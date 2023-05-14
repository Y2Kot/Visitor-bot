package ru.kudryavtsev

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import ru.kudryavtsev.domain.AppContext
import ru.kudryavtsev.model.Range
import ru.kudryavtsev.model.Spreadsheet
import java.io.IOException


class SheetsApi(
    private val context: AppContext
) {
    private val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    private val spreadsheet = Spreadsheet(context.spreadsheetId)

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private val scopes = listOf(SheetsScopes.SPREADSHEETS)
    private val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

    // Load client secrets.
    private val clientSecrets = GoogleClientSecrets.load(jsonFactory, context.credentialsReader)

    private val service: Sheets = Sheets.Builder(httpTransport, jsonFactory, getCredentials())
        .setApplicationName(APPLICATION_NAME)
        .build()

    /**
     * Creates an authorized Credential object.
     *
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
    private fun getCredentials(): Credential {
        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
            .setDataStoreFactory(FileDataStoreFactory(context.tokenDirectory))
            .setAccessType("offline")
            .build()
        val receiver = LocalServerReceiver.Builder().setPort(DEFAULT_PORT).build()
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    fun readLines(range: Range): List<List<Any>> {
        val response = service.spreadsheets().values()[spreadsheet.id, range.data].execute()
        val values = response.getValues()
        return if (values == null || values.isEmpty()) {
            emptyList()
        } else {
            values
        }
    }

    @Throws(IOException::class)
    fun updateValues(range: Range, values: List<List<Any>>) {
        try {
            val body = ValueRange().setValues(values)
            val result = service.spreadsheets().values().update(spreadsheet.id, range.data, body)
                .setValueInputOption(VALUE_INPUT_OPTION)
                .execute()
            System.out.printf("%d cells updated.", result.updatedCells)
        } catch (e: GoogleJsonResponseException) {
            val error = e.details
            if (error.code == 404) {
                System.out.printf("Spreadsheet not found with id '%s'.\n", spreadsheet.id)
            } else {
                throw e
            }
        }
    }

    companion object {
        private const val VALUE_INPUT_OPTION = "USER_ENTERED"

        private const val APPLICATION_NAME = "Google Sheets API Java Quickstart"
        private const val DEFAULT_PORT = 8888
    }
}
