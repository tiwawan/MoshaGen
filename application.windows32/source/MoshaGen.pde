//MoshaGen.pde
//Description:Global variables, setup(), draw() etc.
//Author:tiwawan
//Date:2015/3/26

import controlP5.*;

String filepath = "";
String filename = "";
String outputpath = "";
int n=0;

int paperwidth=3564;
int paperheight=2520;
int margin = 50;
int space = 100;
float mgnf = 2.0;

int guiwidth = 300;
int prevmargin_w = 30;
int prevmargin_h= 50;
int prevspace = 20;
int prevwidth = width-2*prevmargin_w-guiwidth;
int prevheight = height-2*prevmargin_h;
boolean is_image_oblong=true;

boolean separate_mode = false;

//arrangemode 0:horizontal 1:vertical
int arrangemode = 0;
boolean centering = false;

int frame_w = 1;
int frame_h = 1;
int thickness = 5;
boolean extend = false;

int grid_w = 3;
int grid_h = 3;
int grid_thickness = 4;
boolean grid_sqmode = false;
int grid_sqsize = 300;

color color_frame = color(0, 0, 0, 255);
color color_grid = color(0, 0, 0, 128);
color color_bg = color(255, 255, 255, 255);

boolean setup_finished = false;

PGraphics[] dummy;

ImageList il;
ImageDrawer id;
CanvasDrawer cd;

void setup() {
  size(1000, 800);
  background(#9B9B9B);
  frameRate(15);
  if (frame != null) {
    frame.setResizable(true);
  }
  gui();
  dummy = new PGraphics[1];
  dummy[0] = null;
  il = new ImageList();
  id = new ImageDrawer(il);
  cd = new CanvasDrawer(id);
  selectInput("Select a file to process:", "fileSelected");
  setup_finished = true;
}


void draw() {
  background(128);
  if(il.image_loaded){
    id.updateParameters();
    cd.drawCanvas(dummy);
  }
}

void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
    ImageFileInfo imginfo = new ImageFileInfo(selection);
    if(imginfo.pimg_raw == null)return;
    il.addImage(imginfo);
  }
}

void folderSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
    outputpath = selection.getAbsolutePath() + File.separator;
  }
}

void folderSelected_load(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
    il.loadFromDirectory(selection);
  }
}

void dispose() {
  cp5.saveProperties("setting");
}



