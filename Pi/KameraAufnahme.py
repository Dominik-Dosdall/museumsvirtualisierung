from picamera import PiCamera
from time import sleep
import os

camera = PiCamera()

config = open("config_museum.txt","r")

ws = config.readLine().split("=")[1].split("\n")[0]
bucket = config.readLine().split("=")[1].split("\n")[0]
proj = config.readLine().split("=")[1].split("\n")[0]
picName = config.readLine().split("=")[1].split("\n")[0]
objects = config.readLine().split("=")[1].split("\n")[0]
pics = config.readLine().split("=")[1].split("\n")[0]

if not os.path.exists(ws):
	os.mkdir(ws)

if not os.path.exists(ws+"/"+proj):
	os.mkdir(ws+"/"+proj)
else:
	print("error, project already exists")
	exit()

for x in range(0,int(objects)):
	input("Press Enter to start the next camera session")
	os.mkdir(ws+"/"+proj+"/obj_"+str(x))
	os.chdir(ws+"/"+proj+"/obj_"+str(x))
	for y in range(0,int(pics)):
		camera.capture(picName+str(y)+".jpeg","jpeg")
		print("Bild "+str(y)+" aufgenommen")
		sleep(1)
	print("Objekt "+str(x)+" fertig")