from PIL import Image
from pillow_heif import register_heif_opener
import argparse

parser = argparse.ArgumentParser(description='Converting images from heic to jpeg format')
parser.add_argument('--in_image', '-in', help="input heic image name", required=True)
parser.add_argument('--out_image', '-out', help="output jpeg image name", default="out.jpeg")

args = parser.parse_args()
inputFileName = args.in_image
outputFileName = args.out_image

inputFormats = ["heic"]
# TODO add png
outputFormats = ["jpg", "jpeg"]

if all([extension not in inputFileName.lower() for extension in inputFormats]):
    parser.error("Wrong intput file name extension")

if all([extension not in outputFileName.lower() for extension in outputFormats]):
    parser.error("Wrong output file name extension")

register_heif_opener()
image = Image.open(inputFileName)
image.save(outputFileName, format="jpeg")
print("File was successfully converted")
print(outputFileName)
