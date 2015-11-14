import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MoshaGen extends PApplet {

//MoshaGen.pde
//Description:Global variables, setup(), draw() etc.
//Author:tiwawan
//Date:2015/3/26



String filepath = "";
String filename = "";
String outputpath = "";
int n=0;

int paperwidth=3564;
int paperheight=2520;
int margin = 50;
int space = 100;
float mgnf = 2.0f;

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

int color_frame = color(0, 0, 0, 255);
int color_grid = color(0, 0, 0, 128);
int color_bg = color(255, 255, 255, 255);

boolean setup_finished = false;

PGraphics[] dummy;

ImageList il;
ImageDrawer id;
CanvasDrawer cd;

public void setup() {
  size(1000, 800);
  background(0xff9B9B9B);
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


public void draw() {
  background(128);
  if(il.image_loaded){
    id.updateParameters();
    cd.drawCanvas(dummy);
  }
}

public void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
    ImageFileInfo imginfo = new ImageFileInfo(selection);
    if(imginfo.pimg_raw == null)return;
    il.addImage(imginfo);
  }
}

public void folderSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
    outputpath = selection.getAbsolutePath() + File.separator;
  }
}

public void folderSelected_load(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
    il.loadFromDirectory(selection);
  }
}

public void dispose() {
  cp5.saveProperties("setting.ser");
}



//CanvasDrawer.pde
//Description:Class to render in canvas and image
//Author:tiwawan
//Date:2015/3/26

class CanvasDrawer {
  
  ImageDrawer id;
  
  public CanvasDrawer(ImageDrawer idraw) {
    id = idraw;
  }
  
  public void drawCanvas(PGraphics[] graphs) {
    if(separate_mode){
      drawCanvas_sep(graphs);
      return;
    }
    
    PImage image_selected = il.getCurrentImage();

    for(PGraphics g : graphs){
      id.drawBackground(g);
    }

    id.drawImage(0, graphs[0]);
    if(graphs.length >= 2) id.drawImage(1, graphs[1]);
    else id.drawImage(1, null);
    
    int i;
    for(PGraphics g : graphs) {
      id.drawFrame(0, g);
      id.drawFrame(1, g);
      id.drawGrid(0, g);
      id.drawGrid(1, g);
    }
  }
  
  public void drawCanvas_sep(PGraphics[] graphs) {
    PImage image_selected = il.getCurrentImage();
    for(PGraphics g : graphs) {
      id.drawBackground(g);
    }
  
    if(graphs.length==1) {
      id.drawImage(0, null);
      id.drawImage(1, null);
      id.drawFrame(0, null);
      id.drawFrame(1, null);
      id.drawGrid(0, null);
      id.drawGrid(1, null);
    }
    
    else if(graphs.length==3) {
      id.drawImage(0, graphs[0]);
      id.drawImage(1, graphs[2]);
      id.drawFrame(0, graphs[0]);
      id.drawFrame(1, graphs[1]);
      id.drawFrame(1, graphs[2]);
      id.drawGrid(0, graphs[0]);
      id.drawGrid(1, graphs[1]);
      id.drawGrid(1, graphs[2]);
    }
  }
  
  
  public String elimExt(String str) {
    int dot = str.indexOf(".");
    if(dot>0)return str.substring(0, dot);
    else return str;
  }
  
  public String getExt(String str) {
    int dot = str.indexOf(".");
    if(dot>0)return str.substring(dot, str.length());
    else return "";
  }
  
  public void generate() {
    PGraphics[] graphs = new PGraphics[2];
    for(int i=0;i<2;i++){
      graphs[i] = createGraphics(paperwidth, paperheight);
      graphs[i].beginDraw();
    }
    drawCanvas(graphs);
    for(int i=0;i<2;i++){
      graphs[i].endDraw();
    }
    ImageFileInfo info = il.getCurrentInfo();
    String base = elimExt(info.name);
    String ext = getExt(info.name);
    String first = outputpath;
    if(first.length()==0)first = info.dir;
    if(gens_cbox.getState(0) || gens_cbox.getState(1))graphs[0].save(first + base + "_M" + ext);
    if(gens_cbox.getState(2))graphs[1].save(first + base + "_C" + ext);
  }
  
  public void generate_sep() {
    PGraphics[] graphs = new PGraphics[3];
    for(int i=0;i<3;i++){
      graphs[i] = createGraphics(paperwidth, paperheight);
      graphs[i].beginDraw();
    }
    drawCanvas_sep(graphs);
    for(int i=0;i<3;i++){
      graphs[i].endDraw();
    }
    ImageFileInfo info = il.getCurrentInfo();
    String base = elimExt(info.name);
    String ext = getExt(info.name);
    String first = outputpath;
    if(first.length()==0)first = info.dir;
    if(gens_cbox.getState(0))graphs[0].save(first + base + "_M" + ext);
    if(gens_cbox.getState(1))graphs[1].save(first + base + "_S" + ext);
    if(gens_cbox.getState(2))graphs[2].save(first + base + "_C" + ext);
  }
  
  public void generateAll() {
    PGraphics graph;
    il.firstImage();
    for(ImageFileInfo im : il.images) {
      if(separate_mode){
        id.updateParameters();
        generate_sep();
        il.nextImage();
      }
      else {
        id.updateParameters();
        generate();
        il.nextImage();
      }
    }
  }
  
}
//ImageDrawer.pde
//Description:Class to make layout of images and draw them.
//Author:tiwawan
//Date:2015/3/26


class ImageDrawer {
  ImageList il;
  
  float x_s, y_s, w_s, h_s, x_l, y_l, w_l, h_l;
  float fx_s, fy_s, fw_s, fh_s, fx_l, fy_l, fw_l, fh_l;
  int trim_l, trim_r, trim_u, trim_d;
  int trimwidth, trimheight;
  
  PImage image_selected;
  
  public ImageDrawer(ImageList imagelist) {
    il = imagelist;
  }

  public void updateParameters(){
    if(separate_mode) {
      updateParameters_sep();
      return;
    }
    
    updateGuiParams();
    image_selected = il.getCurrentImage();
    ImageFileInfo info = il.getCurrentInfo();
    trim_l = info.trim_l;
    trim_r = info.trim_r;
    trim_u = info.trim_u;
    trim_d = info.trim_d;
    trimwidth = info.getTrimWidth();
    trimheight = info.getTrimHeight();
    float heighttotal, widthtotal;
    float heightmax = paperheight-2*margin;
    float widthmax = paperwidth-2*margin;
    if(arrangemode == 0 && mgnf>1.0f) {
      is_image_oblong = (float)paperwidth/paperheight < ((float)(1+mgnf)*trimwidth/(mgnf*trimheight));
    }
    else if(arrangemode == 0) {
      is_image_oblong = (float)paperwidth/paperheight < ((float)(1+mgnf)*trimwidth/(trimheight));
    }
    else if(arrangemode == 1 && mgnf>1.0f) {
      is_image_oblong = (float)paperwidth/paperheight < ((float)mgnf*trimwidth/((1+mgnf)*trimheight));
    }
    else {
      is_image_oblong = (float)paperwidth/paperheight < ((float)trimwidth/((1+mgnf)*trimheight));
    }
  
    if(arrangemode == 0 && is_image_oblong) {
      w_s = (paperwidth-2*margin-space)/(mgnf+1.0f);
      w_l = mgnf*(paperwidth-2*margin-space)/(mgnf+1.0f);
      h_s = trimheight*w_s/trimwidth;
      h_l = trimheight*w_l/trimwidth;
    }
    else if(arrangemode == 1 && !is_image_oblong){
      h_s = (paperheight-2*margin-space)/(mgnf+1.0f);
      h_l = mgnf*(paperheight-2*margin-space)/(mgnf+1.0f);
      w_l = trimwidth*h_l/trimheight;
      w_s = trimwidth*h_s/trimheight;
    }
    else if(arrangemode == 0 && !is_image_oblong){
      if(mgnf > 1.0f) {
        h_l = heightmax;
        h_s = (float)h_l/mgnf;
      }
      else {
        h_s = heightmax;
        h_l = (float)h_s*mgnf;
      }
      w_l = trimwidth*h_l/trimheight;
      w_s = trimwidth*h_s/trimheight;
      heighttotal = h_s+h_l;
      widthtotal = w_s+w_l;
      if(widthtotal>(widthmax-space)) {
        w_s *= (float)(widthmax-space)/widthtotal;
        w_l *=(float)(widthmax-space)/widthtotal;
        h_s *= (float)(widthmax-space)/widthtotal;
        h_l *=  (float)(widthmax-space)/widthtotal;
      }
    }
    else if(arrangemode == 1 && is_image_oblong){
      if(mgnf > 1.0f) {
        w_l = widthmax;
        w_s = (float)w_l/mgnf;
      }
      else {
        w_s = widthmax;
        w_l = (float)w_s*mgnf;
      }
      h_l = trimheight*w_l/trimwidth;
      h_s = trimheight*w_s/trimwidth;
      heighttotal = h_s+h_l;
      widthtotal = w_s+w_l;
      if(heighttotal>(heightmax-space)) {
        w_s *= (float)(heightmax-space)/heighttotal;
        w_l *=(float)(heightmax-space)/heighttotal;
        h_s *= (float)(heightmax-space)/heighttotal;
        h_l *=  (float)(heightmax-space)/heighttotal;
      }
    }
    prevwidth = width-2*prevmargin_w-guiwidth;
    prevheight = height-2*prevmargin_h;

    if(arrangemode == 0) {
      x_s = margin;
      y_s = margin;
      x_l = margin+space+w_s;
      y_l = margin;
      if(centering) {
        y_s = (float)(paperheight-h_s)/2.0f;
        y_l = (float)(paperheight-h_l)/2.0f;
      }
    }
    else if(arrangemode == 1){
      x_s = margin;
      y_s = margin;
      x_l = margin;
      y_l = margin+space+h_s;
      if(centering) {
        x_s = (float)(paperwidth-w_s)/2.0f;
        x_l = (float)(paperwidth-w_l)/2.0f;
      }
    }

    fx_s=x_s;
    fy_s=y_s;
    fw_s=w_s;
    fh_s=h_s;
    fx_l=x_l;
    fy_l=y_l;
    fw_l=w_l;
    fh_l=h_l;
    
    if(extend){
      if(arrangemode == 0) {
        fy_s = margin;
        fy_l = margin;
        fh_s = paperheight - 2*margin;
        fh_l = paperheight - 2*margin;
      }
      else {
        fx_s = margin;
        fx_l = margin;
        fw_s = paperwidth-2*margin;
        fw_l = paperwidth-2*margin;
      }
    }
  }

  public void updateParameters_sep() {
    updateGuiParams();
    
    image_selected = il.getCurrentImage();
    ImageFileInfo info = il.getCurrentInfo();
    trim_l = info.trim_l;
    trim_r = info.trim_r;
    trim_u = info.trim_u;
    trim_d = info.trim_d;
    trimwidth = info.getTrimWidth();
    trimheight = info.getTrimHeight();
    
    is_image_oblong = (float)(paperwidth-2*margin)/(paperheight-2*margin) < (float)trimwidth/trimheight;
    if(is_image_oblong) {
      if(mgnf >= 1.0f){
        w_l = paperwidth - 2*margin;
        w_s = (float)w_l/mgnf;
      }
      else {
        w_s= paperwidth - 2*margin;
        w_l = (float)w_s*mgnf;
      } 
      h_s = trimheight*(float)w_s/trimwidth;
      h_l = trimheight*(float)w_l/trimwidth;
    }
    else {
      if(mgnf >= 1.0f) {
        h_l = paperheight - 2*margin;
        h_s = h_l/mgnf;
      }
      else {
        h_s = paperheight - 2*margin;
        h_l = h_s*mgnf;
      }
      w_s = trimwidth*(float)h_s/trimheight;
      w_l = trimwidth*(float)h_l/trimheight;
    }
    prevwidth = width-2*prevmargin_w-guiwidth;
    prevheight = height-2*prevmargin_h;
    
    x_s=margin;
    y_s=margin;
    x_l=margin;
    y_l=margin;
    
    if(centering){
      x_s = (paperwidth - w_s)/2;
      y_s = (paperheight - h_s)/2;
      x_l = (paperwidth - w_l)/2;
      y_l = (paperheight - h_l)/2;
    }
    
    fx_s=x_s;
    fy_s=y_s;
    fw_s=w_s;
    fh_s=h_s;
    fx_l=x_l;
    fy_l=y_l;
    fw_l=w_l;
    fh_l=h_l;
   
    if(extend){
      fy_s = margin;
      fy_l = margin;
      fh_s = paperheight - 2*margin;
      fh_l = paperheight - 2*margin;
      fx_s = margin;
      fx_l = margin;
      fw_s = paperwidth-2*margin;
      fw_l = paperwidth-2*margin;
    }
  }

  public void updateGuiParams() {
    int cpre = (int)cpre_ddlist.getValue();
    if(cpre != last_paper && cpre != 0){
      paperwidth = paper_presets[cpre][0];
      cwidth_nbox.setValue(paperwidth);
      paperheight = paper_presets[cpre][1];
      cheight_nbox.setValue(paperheight);
    }
    else {
      paperwidth = (int)cwidth_nbox.getValue();
      paperheight = (int)cheight_nbox.getValue();
      cpre_ddlist.setIndex(0);
    }
    last_paper = cpre;
  
    separate_mode = separate_cbox.getState(0);

    if(separate_mode){
      arrange_rbutton.hide();
      space_nbox.hide();
    }
    else {
      arrange_rbutton.show();
      space_nbox.show();
    }
  
    if(arrange_rbutton.getState(0))arrangemode = 0;
    else arrangemode = 1;
  
    centering = centering_cbox.getState(0);
    
    mgnf = mgnf_slider.getValue();
    
    margin = (int)margin_nbox.getValue();
    space = (int)margin_nbox.getValue();
  
    thickness = (int)thickness_slider.getValue();
    
    if(lvisible_cbox.getState(0)) frame_w = 1;
    else frame_w = 0;
  
    if(lvisible_cbox.getState(1)) frame_h = 1;
    else frame_h = 0;
  
    extend = extend_rbutton.getState(0);
    
    grid_sqmode = grid_sq_cbox.getState(0);
    grid_thickness = (int)grid_thickness_slider.getValue();
    grid_w = (int)grid_horizontal_slider.getValue();
    grid_h = (int)grid_vertical_slider.getValue();
    if(grid_sqmode){
      grid_vertical_slider.hide();
      grid_horizontal_slider.hide();
      grid_sqsize_nbox.show();
      grid_sqsize = (int)grid_sqsize_nbox.getValue();
    }
    else {
      grid_vertical_slider.show();
      grid_horizontal_slider.show();
      grid_sqsize_nbox.hide();
    }

    color_bg = cpicker_bg.getColorValue();
    color_frame = cpicker_frame.getColorValue();
    color_grid = cpicker_grid.getColorValue();
    cpicker_bg.hide();
    cpicker_frame.hide();
    cpicker_grid.hide();
    if((int)color_ddlist.getValue()==0)cpicker_frame.show();
    else if((int)color_ddlist.getValue()==1)cpicker_grid.show();
    else cpicker_bg.show();
    
    il.setTrim_l((int)trim_l_nbox.getValue());
    il.setTrim_u((int)trim_u_nbox.getValue());
    il.setTrim_r((int)trim_r_nbox.getValue());
    il.setTrim_d((int)trim_d_nbox.getValue());
    il.setTrimRange();
  }
  
  // scale and translate x position to preview space
  public float prev_x(float p) {
    p = prev_s(p);
    p += guiwidth+prevmargin_w;
    return p;
  }

  // scale and translate y position to preview space
  public float prev_y(float p) {
    p = prev_s(p);
    p += prevmargin_h;
    return p;
  }

  // scale to preview space
  public float prev_s(float p) {
    if((float)prevwidth/prevheight < (float)paperwidth/paperheight) p = p * (float)prevwidth/paperwidth;
    else p = p * (float)prevheight/paperheight;
    if(p>=0)return p;
    else return -p;
  }

  public void image_prev(PImage im, float x, float y, float w, float h, int u1, int v1, int u2, int v2) {
    image(im, prev_x(x), prev_y(y), prev_s(w), prev_s(h), u1, v1, u2, v2);
  }

  public void rect_prev(float x, float y, float w, float h) {
    rect(prev_x(x), prev_y(y), prev_s(w), prev_s(h));
  }

  public void rect_prev_r(float x, float y, float w, float h, float r) {
    rect(prev_x(x), prev_y(y), prev_s(w), prev_s(h), prev_s(r));
  }

  public void line_prev(float x1, float y1, float x2, float y2) {
    line(prev_x(x1), prev_y(y1), prev_x(x2), prev_y(y2));
  }

  public void strokeWeight_prev(float t) {
    strokeWeight(prev_s(t));
  }
  
  public float prev_x_1(float p) {
    p = prev_s_sep(p);
    p += guiwidth+prevmargin_w;
    return p;
  }

  // scale and translate y position to preview space
  public float prev_y_1(float p) {
    p = prev_s_sep(p);
    p += prevmargin_h;
    return p;
  }

  public float prev_x_2(float p) {
    p = prev_s_sep(p);
    p += guiwidth+prev_s_sep(paperwidth)+prevspace+prevmargin_w;
    return p;
  }

  // scale and translate y position to preview space
  public float prev_y_2(float p) {
    p = prev_s_sep(p);
    p += prevmargin_h;
    return p;
  }

  // scale to preview space
  public float prev_s_sep(float p) {
    if((float)prevwidth/prevheight < (float)(paperwidth*2)/paperheight) p = p * (float)prevwidth/(paperwidth*2);
    else p= p * (float)prevheight/paperheight;
    //p = p * ((float)prevwidth/(paperwidth*2));
    if(p>=0)return p;
    else return -p;
  }

  public void image_prev_1(PImage im, float x, float y, float w, float h, int u1, int v1, int u2, int v2) {
    image(im, prev_x_1(x), prev_y_1(y), prev_s_sep(w), prev_s_sep(h), u1, v1, u2, v2);
  }

  public void image_prev_2(PImage im, float x, float y, float w, float h, int u1, int v1, int u2, int v2) {
    image(im, prev_x_2(x), prev_y_2(y), prev_s_sep(w), prev_s_sep(h), u1, v1, u2, v2);
  }

  public void rect_prev_1(float x, float y, float w, float h) {
    rect(prev_x_1(x), prev_y_1(y), prev_s_sep(w), prev_s_sep(h));
  }

  public void rect_prev_2(float x, float y, float w, float h) {
    rect(prev_x_2(x), prev_y_2(y), prev_s_sep(w), prev_s_sep(h));
  }

  public void rect_prev_r_1(float x, float y, float w, float h, float r) {
    rect(prev_x_1(x), prev_y_1(y), prev_s_sep(w), prev_s_sep(h), prev_s_sep(r));
  }

  public void rect_prev_r_2(float x, float y, float w, float h, float r) {
    rect(prev_x_1(x), prev_y_1(y), prev_s_sep(w), prev_s_sep(h), prev_s_sep(r));
  }

  public void line_prev_1(float x1, float y1, float x2, float y2) {
    line(prev_x_1(x1), prev_y_1(y1), prev_x_1(x2), prev_y_1(y2));
  }

  public void line_prev_2(float x1, float y1, float x2, float y2) {
    line(prev_x_2(x1), prev_y_2(y1), prev_x_2(x2), prev_y_2(y2));
  }

  public void strokeWeight_prev_sep(float t) {
    strokeWeight(prev_s_sep(t));
  }
  
  public void image_swtch(int target, PImage im, float x, float y, float w, float h, int u1, int v1, int u2, int v2, PGraphics graph) {
    if(graph == null) {
      switch(target) {
        case 0:
          image_prev(im, x, y, w, h, u1, v1, u2, v2);
          break;
        case 1:
          image_prev_1(im, x, y, w, h, u1, v1, u2, v2);
          break;
        case 2:
          image_prev_2(im, x, y, w, h, u1, v1, u2, v2);
          break;
      }
    }
    else graph.image(im, x, y, w, h, u1, v1, u2, v2);
  }

  public void rect_swtch(int target, float x, float y, float w, float h, PGraphics graph) {
    if(graph == null) {
      switch(target) {
        case 0:
          rect_prev(x, y, w, h);
          break;
        case 1:
          rect_prev_1(x, y, w, h);
          break;
        case 2:
          rect_prev_2(x, y, w, h);
          break;
      }
    }
    else graph.rect(x, y, w, h);
  }

  public void line_swtch(int target, float x1, float y1, float x2, float y2, PGraphics graph) {
    if(graph == null) {
      switch(target) {
        case 0:
          line_prev(x1, y1, x2, y2);
          break;
        case 1:
          line_prev_1(x1, y1, x2, y2);
          break;
        case 2:
          line_prev_2(x1, y1, x2, y2);
          break;
      }
    }
    else graph.line(x1, y1, x2, y2);
  }

  public void fill_swtch(int c, PGraphics graph) {
    if(graph == null) fill(c);
    else graph.fill(c);
  }

  public void strokeWeight_swtch(int target, float t, PGraphics graph) {
    if(graph == null){
      if(target == 0)strokeWeight_prev(t);
      else strokeWeight_prev_sep(t);
    }
    else graph.strokeWeight(t);
  }

  public void stroke_swtch(int c, PGraphics graph) {
    if(graph == null) stroke(c);
    else graph.stroke(c);
  }

  public void noStroke_swtch(PGraphics graph) {
    if(graph == null) noStroke();
    else graph.noStroke();
  }
  
  public void drawFrame_swtch(int target, float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2, PGraphics g) {
    if(frame_w == 1 && !(arrangemode==0 && extend && target==0) ){
      rect_swtch(target, x1-thickness*frame_h, y1-thickness, w1+2*thickness*frame_h, thickness, g);
      rect_swtch(target, x1-thickness*frame_h, y1+h1, w1+2*thickness*frame_h, thickness, g);
    }
    if(frame_h == 1 && !(arrangemode==1 && extend && target==0) ){
      rect_swtch(target, x2-thickness, y2-thickness*frame_w, thickness, h2+2*thickness*frame_w, g);
      rect_swtch(target, x2+w2, y2-thickness*frame_w, thickness, h2+2*thickness*frame_w, g);
    }
  }

  public void drawGrid_swtch(int target, float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2, PGraphics g) {
    stroke_swtch(color_grid, g);
    strokeWeight_swtch(target, grid_thickness, g);
    int i;
    for(i=1;i<=grid_w;i++){
      line_swtch(target, x1, y1+i*h1/(grid_w+1), x1+w1, y1+i*h1/(grid_w+1), g);
    }
    for(i=1;i<=grid_h;i++){
      line_swtch(target, x2+i*w2/(grid_h+1), y2, x2+i*w2/(grid_h+1), y2+h2, g);
    }
  }
  
  public void drawSquareGrid_swtch(int target,  float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2, float size, PGraphics g) {
    stroke_swtch(color_grid, g);
    strokeWeight_swtch(target, grid_thickness, g);
    float f = size;
    while(f<h1) {
      line_swtch(target, x1, y1+f, x1+w1, y1+f, g);
      f+=size;
    }
    f = size;
    while(f<w2) {
      line_swtch(target, x2+f, y2, x2+f, y2+h2, g);
      f+=size;
    }
  }
  
  public void drawBackground(PGraphics pg) {
    noStroke_swtch(pg);
    fill_swtch(color_bg, pg);
    if(!separate_mode) {
      rect_swtch(0, 0, 0, paperwidth, paperheight, pg);
    }
    else {
      rect_swtch(1, 0, 0, paperwidth, paperheight, pg);
      rect_swtch(2, 0, 0, paperwidth, paperheight, pg);
    }
  }

  //size 0:small 1:large
  public void drawFrame(int size, PGraphics pg) {
    fill_swtch(color_frame, pg);
    if(!separate_mode) {
      if(size==0) drawFrame_swtch(0, fx_s, y_s, fw_s, h_s, x_s, fy_s, w_s, fh_s, pg);
      else drawFrame_swtch(0, fx_l, y_l, fw_l, h_l, x_l, fy_l, w_l, fh_l, pg);
    }
    else {
      if(size==0) drawFrame_swtch(1, fx_s, y_s, fw_s, h_s, x_s, fy_s, w_s, fh_s, pg);
      else drawFrame_swtch(2, fx_l, y_l, fw_l, h_l, x_l, fy_l, w_l, fh_l, pg);
    }
  }
  
  public void drawGrid(int size, PGraphics pg) {
    if(grid_sqmode) {
      drawSquareGrid(size, pg);
      return;
    }
    strokeWeight_swtch(0, grid_thickness, pg);
    stroke_swtch(color_grid, pg);
    if(!separate_mode) {
      if(size==0) drawGrid_swtch(0, fx_s, y_s, fw_s, h_s, x_s, fy_s, w_s, fh_s, pg);
      else drawGrid_swtch(0, fx_l, y_l, fw_l, h_l, x_l, fy_l, w_l, fh_l, pg);
    }
    else {
      if(size==0)drawGrid_swtch(1, fx_s, y_s, fw_s, h_s, x_s, fy_s, w_s, fh_s, pg);
      else drawGrid_swtch(2, fx_l, y_l, fw_l, h_l, x_l, fy_l, w_l, fh_l, pg);
    }
  }
  
  public void drawSquareGrid(int size, PGraphics pg) {
    strokeWeight_swtch(0, grid_thickness, pg);
    stroke_swtch(color_grid, pg);
    if(!separate_mode) {
      if(size==0) drawSquareGrid_swtch(0, fx_s, y_s, fw_s, h_s, x_s, fy_s, w_s, fh_s, (float)grid_sqsize/mgnf, pg);
      else drawSquareGrid_swtch(0, fx_l, y_l, fw_l, h_l, x_l, fy_l, w_l, fh_l, (float)grid_sqsize, pg);
    }
    else {
      if(size==0)drawSquareGrid_swtch(1, fx_s, y_s, fw_s, h_s, x_s, fy_s, w_s, fh_s, (float)grid_sqsize/mgnf, pg);
      else drawSquareGrid_swtch(2, fx_l, y_l, fw_l, h_l, x_l, fy_l, w_l, fh_l, (float)grid_sqsize, pg);
    }
  }
  
  public void drawImage(int size, PGraphics pg) {
    if(!separate_mode) {
      if(size==0)image_swtch(0, image_selected, x_s, y_s, w_s, h_s, trim_l, trim_u, trim_r, trim_d, pg);
      else image_swtch(0, image_selected, x_l, y_l, w_l, h_l, trim_l, trim_u, trim_r, trim_d, pg);
    }
    else {
      if(size==0)image_swtch(1, image_selected, x_s, y_s, w_s, h_s, trim_l, trim_u, trim_r, trim_d, pg);
      else image_swtch(2, image_selected, x_l, y_l, w_l, h_l, trim_l, trim_u, trim_r, trim_d, pg);
    }
  }
}
//ImageFileInfo.pde
//Description:Container of PImage and settings depend on it.
//Author:tiwawan
//Date:2015/3/26




class ImageFileInfo {
  public PImage pimg;
  public PImage pimg_raw = null;
  public String path;
  public String name;
  public String dir;
  
  public int width_raw;
  public int height_raw;
  
  public int trim_l = 0;
  public int trim_r = 0;
  public int trim_u = 0;
  public int trim_d = 0;
  
  public float contrast = 0;
  public float brightness = 0;
  boolean isgray = false;
  
  public ImageFileInfo (File f) {
    path = f.getAbsolutePath();
    pimg_raw = loadImage(path);
    if(pimg_raw == null)return;
    pimg = pimg_raw.get();
    name = f.getName();
    dir = f.getParent() + File.separator;
    width_raw = pimg.width;
    height_raw = pimg.height;
    trim_l = 0;
    trim_u = 0;
    trim_r = width_raw;
    trim_d = height_raw;
  }
  
  public ImageFileInfo (String name_, String dir_) {
    pimg_raw = loadImage(dir_ + File.separator + name_);
    if(pimg_raw == null)return;
    pimg = pimg_raw.get();
    name = name_;
    dir = dir_ + File.separator;
    width_raw = pimg.width;
    height_raw = pimg.height;
    trim_l = 0;
    trim_u = 0;
    trim_r = width_raw;
    trim_d = height_raw;
  }
  
  public void setTrims(int l, int u, int r, int d) {
    trim_l = l;
    trim_r = r;
    trim_u = u;
    trim_d = d;
  }
  
  public void setTrim_l(int n) {
    trim_l=n;
  }
  
  public void setTrim_u(int n) {
    trim_u=n;
  }
  
  public void setTrim_r(int n) {
    trim_r=n;
  }
  
  public void setTrim_d(int n) {
    trim_d=n;
  }
  
  public int getTrimWidth() {
    return trim_l-trim_r;
  }
  
  public int getTrimHeight() {
    return trim_u-trim_d;
  }
  
  public void resetTrim() {
    setTrims(0, 0, width_raw, height_raw);
  }
  
  public void autoTrim() {
    resetTrim();
    pimg.loadPixels();
    int l=0, u=0, r=width_raw, d=height_raw;
    int bcolor = pimg.pixels[0];
    boolean endflg_l = false;
    boolean endflg_u = false;
    boolean endflg_r = false;
    boolean endflg_d = false;
    for(int i=0;i<height_raw;i++){
      for(int j=0;j<width_raw;j++){
        if(pimg.pixels[i*width_raw+j]!=bcolor) endflg_u=true;
        if(pimg.pixels[width_raw*height_raw-1-(i*width_raw+j)]!=bcolor) endflg_d=true;
      }
      if(!endflg_u) u++;
      if(!endflg_d) d--;
      if(endflg_u && endflg_d)break;
    }
    for(int j=0;j<width_raw;j++){
      for(int i=0;i<height_raw;i++){
        if(pimg.pixels[i*width_raw+j]!=bcolor) endflg_l=true;
        if(pimg.pixels[width_raw*height_raw-1-(i*width_raw+j)]!=bcolor) endflg_r=true;
      }
      if(!endflg_l) l++;
      if(!endflg_r) r--;
      if(endflg_l && endflg_r)break;
    }
    if(l>r || u>d)return;
    setTrims(l,u,r,d);
  }
  
  public void filterImage() {
    contrast = contrast_nbox.getValue();
    brightness = brightness_nbox.getValue();
    isgray = gray_cbox.getState(0);
    pimg_raw.loadPixels();
    pimg.loadPixels();
    float[] pix = {0.0f, 0.0f, 0.0f, 0.0f};
    float a = 255.0f/(255.0f-2*contrast);
    for(int i=0; i<width_raw*height_raw; i++) {
      pix[0] = a*(red(pimg_raw.pixels[i])-contrast)+brightness;
      pix[1] = a*(green(pimg_raw.pixels[i])-contrast)+brightness;
      pix[2] = a*(blue(pimg_raw.pixels[i])-contrast)+brightness;
      pix[3] = alpha(pimg_raw.pixels[i]);
      for(int j=0;j<3;j++){
        if(pix[j]<0)pix[j]=0;
        else if(pix[j]>255)pix[j]=255;
      }
      pimg.pixels[i] = color(pix[0], pix[1], pix[2], pix[3]);
    }
    pimg.updatePixels();
    if(isgray)pimg.filter(GRAY);
  }
  
  
}
//ImageList.pde
//Description:Class to load and manage images.
//Author:tiwawan
//Date:2015/3/26


class ImageList {
  
  boolean image_loaded = false;
  public ArrayList<ImageFileInfo> images;
  PImage image_selected;
  int image_selected_index = 0;
  ImageFileInfo info_selected;

  int i=0;

  public ImageList() {
    images = new ArrayList<ImageFileInfo>();
  }
  
  public void addImage(ImageFileInfo imginfo) {
    image_selected_index = images.size();
    images.add(imginfo);
    image_selected = imginfo.pimg;
    info_selected = imginfo;
    fcursor_tarea.setText("" + (image_selected_index+1) + "/" + images.size());
    setTrimGui();
    setFilter();
    image_loaded = true;
  }
  
  public void deleteImage() {
    if(images.size()==0)return;
    images.remove(image_selected_index);
    if(image_selected_index>0)image_selected_index--;
    if(images.size()>0){
      info_selected = images.get(image_selected_index);
      image_selected = info_selected.pimg;
      fcursor_tarea.setText("" + (image_selected_index+1) + "/" + images.size());
      setTrimGui();
    }
    else {
      image_selected = null;
      image_loaded = false;
      fcursor_tarea.setText("0/0");
    }
  }
  
  public void firstImage() {
    if(!image_loaded)return;
    image_selected_index = 0;
    info_selected = images.get(image_selected_index);
    image_selected = info_selected.pimg;
    fcursor_tarea.setText("" + (image_selected_index+1) + "/" + images.size());
    setTrimGui();
    setFilter();
  }

  public void nextImage() {
    if(!image_loaded || image_selected_index == images.size()-1)return;
    image_selected_index++;
    info_selected = images.get(image_selected_index);
    image_selected = info_selected.pimg;
    fcursor_tarea.setText("" + (image_selected_index+1) + "/" + images.size());
    setTrimGui();
    setFilter();
  }

  public void prevImage() {
    if(!image_loaded || image_selected_index == 0)return;
    image_selected_index--;
    info_selected = images.get(image_selected_index);
    image_selected = info_selected.pimg;
    fcursor_tarea.setText("" + (image_selected_index+1) + "/" + images.size());
    setTrimGui();
    setFilter();
  }
  
  public ImageFileInfo getCurrentInfo() {
    return info_selected;
  }
  
  public PImage getCurrentImage() {
    return image_selected;
  }
  
  public void setTrimGui() {
    setTrimRange();
    trim_l_nbox.setValue(info_selected.trim_l);
    trim_u_nbox.setValue(info_selected.trim_u);
    trim_r_nbox.setValue(info_selected.trim_r);
    trim_d_nbox.setValue(info_selected.trim_d);
  }
  
  public void setTrimRange(){
    trim_l_nbox.setRange(0, info_selected.trim_r);
    trim_u_nbox.setRange(0, info_selected.trim_d);
    trim_r_nbox.setRange(info_selected.trim_l, info_selected.width_raw);
    trim_d_nbox.setRange(info_selected.trim_u, info_selected.height_raw);
  }
  
  public void setTrim_l(int n) {
    info_selected.setTrim_l(n);
  }
  
  public void setTrim_u(int n) {
    info_selected.setTrim_u(n);
  }
  
  public void setTrim_r(int n) {
    info_selected.setTrim_r(n);
  }
  
  public void setTrim_d(int n) {
    info_selected.setTrim_d(n);
  }
  
  public void filterImage() {
    info_selected.filterImage();
  }
  
  public void resetTrim() {
    info_selected.resetTrim();
    setTrimGui();
  }
  
  public void autoTrim() {
    info_selected.autoTrim();
    setTrimGui();
  }
  
  public void setFilter() {
    contrast_nbox.setValue(info_selected.contrast);
    brightness_nbox.setValue(info_selected.brightness);
    if(info_selected.isgray)gray_cbox.activate(0);
    else gray_cbox.deactivate(0);
  }
  
  public void loadFromDirectory(File selection) {
    String[] filelist = selection.list();
    String dir_ = selection.getAbsolutePath();
    ImageFileInfo im;
    for(String s : filelist) {
      im = new ImageFileInfo(s, dir_);
      if(im.pimg_raw != null)addImage(im);
    }
  }
  
}
//gui.pde
//Description:gui() setups gui.
//Author:tiwawan
//Date:2015/3/26

int last_paper = 0;
int paper_presets[][] = { {1, 1},
                          {7016, 4961},
                          {4209, 2976},
                          {3508, 2480},
                          {1684, 1191},
                          {847, 595},
                          {6071, 4299},
                          {3643, 2580},
                          {3035, 2150},
                          {2024, 1433},
                          {1457, 1032},
                          {729, 516}
                        };

ControlP5 cp5;
Accordion accordion;
Textarea fcursor_tarea;

//File Input/Output
CheckBox gens_cbox;

//Canvas Settings
Numberbox cwidth_nbox;
Numberbox cheight_nbox;
CheckBox separate_cbox;
DropdownList cpre_ddlist;

//Arrangement
CheckBox centering_cbox;
Slider mgnf_slider;
RadioButton arrange_rbutton;
Numberbox margin_nbox;
Numberbox space_nbox;

//Frame
Slider thickness_slider;
RadioButton extend_rbutton;
CheckBox lvisible_cbox;

//Grid
CheckBox grid_sq_cbox;
Numberbox grid_sqsize_nbox;
Slider grid_horizontal_slider;
Slider grid_vertical_slider;
Slider grid_thickness_slider;


//Color
DropdownList color_ddlist;
ColorPicker cpicker_frame;
ColorPicker cpicker_grid;
ColorPicker cpicker_bg;

//Trimming
Numberbox trim_l_nbox;
Numberbox trim_u_nbox;
Numberbox trim_r_nbox;
Numberbox trim_d_nbox;

//Filter
Numberbox contrast_nbox;
Numberbox brightness_nbox;
CheckBox gray_cbox;


ControlFont cf;


public void gui() {
  cf = new ControlFont(createFont("Arial",11));
  cf.sharp();
  cp5 = new ControlP5(this, cf);


  int bar_height=15;

  Group g_file = cp5.addGroup("File Input/Output")
                      .setBackgroundColor(color(0, 128))
                      .setBarHeight(bar_height)
                      ;
  Group g_canvas = cp5.addGroup("Canvas Setting")
                      .setBackgroundColor(color(0, 128))
                      .setBarHeight(bar_height)
                      ;
  Group g_arrange = cp5.addGroup("Arrangement")
                       .setBackgroundColor(color(0, 128))
                       .setBarHeight(bar_height)
                       ;
  Group g_frame = cp5.addGroup("Frame")
                     .setBackgroundColor(color(0, 128))
                     .setBarHeight(bar_height)
                     ;
  Group g_grid = cp5.addGroup("Grid")
                     .setBackgroundColor(color(0, 128))
                     .setBarHeight(bar_height)
                     ;
  Group g_color = cp5.addGroup("Color")
                     .setBackgroundColor(color(0, 128))
                     .setBarHeight(bar_height)
                     ;
  Group g_trim = cp5.addGroup("Trimming")
                     .setBackgroundColor(color(0, 128))
                     .setBarHeight(bar_height)
                     ;
  Group g_filter = cp5.addGroup("Filter")
                     .setBackgroundColor(color(0, 128))
                     .setBarHeight(bar_height)
                     ;
                     
  cp5.addButton("< Prev")
     .setPosition(guiwidth+prevmargin_w, 10)
     .setSize(50, 30)
     ;
  cp5.addButton("Next >")
     .setPosition(guiwidth+prevmargin_w+60, 10)
     .setSize(50, 30)
     ;
  fcursor_tarea = cp5.addTextarea("File Cursor")
                     .setPosition(guiwidth+prevmargin_w+120, 15)
                     .setText("0/0")
                     ;

  CColor opcolor = new CColor(0xffB21B85, 0xff6F0D52, 0xffB21B85, 0xffFFFFFF, 0xffFFFFFF);

  // GUI around file
  cp5.addButton("Open File")
     .setPosition(10, 10)
     .moveTo(g_file)
     .setColor(opcolor)
     .getCaptionLabel().align(CENTER, CENTER)
     ;
  cp5.addButton("Open Directory")
     .setPosition(90, 10)
     .setSize(120, 20)
     .moveTo(g_file)
     .setColor(opcolor)
     .getCaptionLabel().align(CENTER, CENTER)
     ;
  cp5.addButton("Close File")
     .setPosition(220, 10)
     .moveTo(g_file)
     .getCaptionLabel().align(CENTER, CENTER)
     ;
     
  cp5.addButton("Output Directory")
     .setPosition(10, 35)
     .setSize(130, 20)
     .moveTo(g_file)
     .getCaptionLabel().align(CENTER, CENTER)
     ;
     
  CColor gcolor = new CColor(0xff9DE815, 0xff15B202, 0xff9DE815, 0xffFFFFFF, 0xffFFFFFF);
  
  cp5.addButton("Generate")
     .setPosition(100, 70)
     .setSize(80,40)
     .moveTo(g_file)
     .setColor(gcolor)
     .getCaptionLabel().align(CENTER, CENTER)
     ;
  cp5.addButton("Generate\nAll")
     .setPosition(200, 70)
     .setSize(80,40)
     .moveTo(g_file)
     .setColor(gcolor)
     .getCaptionLabel().align(CENTER, UP)
     ;
  gens_cbox = cp5.addCheckBox("Generate Things")
                     .setPosition(10, 60)
                     .setSize(20, 20)
                     .addItem("Model", 0)
                     .addItem("Sheet", 1)
                     .addItem("Check", 2)
                     .activate(0)
                     .activate(1)
                     .activate(2)
                     .moveTo(g_file)
                     ;
    
 // GUI around canvas
  cwidth_nbox = cp5.addNumberbox("Width")
                   .setPosition(10, 10)
                   .setLabel("Width(px)")
                   .setSize(100, 20)
                   .setRange(1.0f, 20000.0f)
                   .setValue(3564.0f)
                   .updateDisplayMode(1)
                   .setDirection(Controller.HORIZONTAL)
                   .moveTo(g_canvas)
                   ;
                   
  cheight_nbox = cp5.addNumberbox("Height")
                    .setPosition(10, 50)
                    .setLabel("Height(px)")
                    .setSize(100, 20)
                    .setRange(1.0f, 20000.0f)
                    .setValue(2520.0f)
                    .setDirection(Controller.HORIZONTAL)
                    .moveTo(g_canvas)
                    ;
                    
  separate_cbox = cp5.addCheckBox("Separate")
                     .setPosition(120, 50)
                     .setSize(20, 20)
                     .addItem("Separate Sheet", 0)
                     .activate(0)
                     .moveTo(g_canvas)
                     ;
                     
    cp5.addButton("Swap")
     .setPosition(220, 10)
     .setSize(50, 30)
     .moveTo(g_canvas)
     ;
  cpre_ddlist = cp5.addDropdownList("Canvas Presets")
                   .setPosition(120, 30)
                   .setSize(80, 80)
                   .moveTo(g_canvas)
                   .setBarHeight(20)
                   ;
  cpre_ddlist.setMousePressed(true);
  cpre_ddlist.addItem("Presets", 0);
  cpre_ddlist.addItem("A4 600dpi", 1);
  cpre_ddlist.addItem("A4 360dpi", 2);
  cpre_ddlist.addItem("A4 300dpi", 3);
  cpre_ddlist.addItem("A4 144dpi", 4);
  cpre_ddlist.addItem("A4 72dpi", 5);
  cpre_ddlist.addItem("B5 600dpi", 6);
  cpre_ddlist.addItem("B5 360dpi", 7);
  cpre_ddlist.addItem("B5 300dpi", 8);
  cpre_ddlist.addItem("B5 144dpi", 9);
  cpre_ddlist.addItem("B5 72dpi", 10);
  cpre_ddlist.setIndex(0);
     
  //GUI around arrangement
  mgnf_slider = cp5.addSlider("scaling")
                   .setLabel("Scale")
                   .setPosition(10,10)
                   .setSize(150,20)
                   .setRange(0.1f,4.0f)
                   .setValue(2.0f)
                   .setScrollSensitivity(0.1f)
                   .setSliderMode(Slider.FIX)
                   .setNumberOfTickMarks(40)
                   .moveTo(g_arrange)
                   ;
  arrange_rbutton = cp5.addRadioButton("Arrange Direction")
                  .setPosition(10,50)
                  .setSize(20,20)
                  .addItem("Left to right", 0)
                  .addItem("Up to down", 1)
                  .activate(0)
                  .setNoneSelectedAllowed(false)
                  .moveTo(g_arrange)
                  ;
  margin_nbox = cp5.addNumberbox("Margin")
                   .setPosition(140, 50)
                   .setSize(50, 20)
                   .setRange(1,1000)
                   .setValue(50)
                   .setDirection(Controller.HORIZONTAL)
                   .moveTo(g_arrange)
                   ;
  space_nbox = cp5.addNumberbox("Space")
                   .setPosition(200, 50)
                   .setSize(60, 20)
                   .setRange(1,1000)
                   .setValue(100)
                   .setDirection(Controller.HORIZONTAL)
                   .moveTo(g_arrange)
                   ;
  centering_cbox = cp5.addCheckBox("Center")
                       .setPosition(210,10)
                       .setSize(20,20)
                       .addItem("Centering", 0)
                       .activate(0)
                       .moveTo(g_arrange)
                       ;

  //GUI around frame
  thickness_slider = cp5.addSlider("Thickness")
                         .setPosition(10,10)
                         .setSize(150,20)
                         .setRange(0,20.0f)
                         .setValue(5.0f)
                         .setScrollSensitivity(0.1f)
                         .setSliderMode(Slider.FIX)
                         .setNumberOfTickMarks(21)
                         .moveTo(g_frame)
                         ;
  lvisible_cbox = cp5.addCheckBox("Line Visible")
                     .setPosition(10,50)
                     .setSize(20,20)
                     .addItem("Horizontal", 0)
                     .activate(0)
                     .addItem("Vertical", 1)
                     .activate(1)
                     .moveTo(g_frame)
                     ;
  extend_rbutton = cp5.addRadioButton("ExtendButton")
                       .setPosition(130,50)
                       .setSize(20,20)
                       .addItem("Extend", 0)
                       .deactivate(0)
                       .moveTo(g_frame)
                       ;
     
  grid_sq_cbox = cp5.addCheckBox("Square Grid")
                    .setPosition(10, 10)
                    .setSize(18,18)
                    .addItem("Square", 0)
                    .moveTo(g_grid)
                    ;

  grid_sqsize_nbox = cp5.addNumberbox("Square Size")
                        .setPosition(90, 10)
                        .setLabel("Size")
                        .moveTo(g_grid)
                        .setValue(300)
                        .setRange(1, 10000)
                        .setDirection(Controller.HORIZONTAL)
                        .hide()
                        ;
  grid_horizontal_slider = cp5.addSlider("Grid Horizontal")
                               .setLabel("Horizontal")
                               .setPosition(80,10)
                               .setSize(140,18)
                               .setRange(0,15)
                               .setValue(3.0f)
                               .setScrollSensitivity(0.1f)
                               .setSliderMode(Slider.FIX)
                               .setNumberOfTickMarks(16)
                               .moveTo(g_grid)
                               ;
  grid_vertical_slider = cp5.addSlider("Grid Vertical")
                             .setLabel("Vertical")
                             .setPosition(80,40)
                             .setSize(140,18)
                             .setRange(0,15)
                             .setValue(3.0f)
                             .setScrollSensitivity(0.1f)
                             .setSliderMode(Slider.FIX)
                             .setNumberOfTickMarks(16)
                             .moveTo(g_grid)
                             ;
  grid_thickness_slider = cp5.addSlider("Grid Thickness")
                             .setLabel("Thickness")
                             .setPosition(80,70)
                             .setSize(140,18)
                             .setRange(0,20)
                             .setValue(4.0f)
                             .setScrollSensitivity(0.1f)
                             .setSliderMode(Slider.FIX)
                             .setNumberOfTickMarks(21)
                             .moveTo(g_grid)
                             ;
     
       
  cpicker_frame = cp5.addColorPicker("Color Picker Frame")
                     .setPosition(10,20)
                     .moveTo(g_color)
                     .setColorValue(color_frame)
                     ;
  cpicker_grid = cp5.addColorPicker("Color Picker Grid")
                    .setPosition(10,20)
                    .moveTo(g_color)
                    .setColorValue(color_grid)
                    .hide()
                    ;
  cpicker_bg = cp5.addColorPicker("Color Picker BG")
                  .setPosition(10,20)
                  .moveTo(g_color)
                  .setColorValue(color_bg)
                  .hide()
                  ;
  color_ddlist = cp5.addDropdownList("What color")
                     .setPosition(10,20)
                     .setWidth(100)
                     .moveTo(g_color)
                     .setBarHeight(bar_height)
                     ;
  color_ddlist.addItem("Frame", 0);
  color_ddlist.addItem("Grid", 1);
  color_ddlist.addItem("Background", 2);
  color_ddlist.setIndex(0);
  
  trim_l_nbox = cp5.addNumberbox("Trimming Left")
                   .setLabel("Left")
                   .setPosition(10, 30)
                   .setWidth(60)
                   .setValue(0)
                   .moveTo(g_trim)
                   ;
  trim_u_nbox = cp5.addNumberbox("Trimming Up")
                   .setLabel("Up")
                   .setPosition(80, 10)
                   .setWidth(60)
                   .setValue(0)
                   .moveTo(g_trim)
                   ;
  trim_r_nbox = cp5.addNumberbox("Trimming Right")
                   .setLabel("Right")
                   .setPosition(150, 30)
                   .setWidth(60)
                   .setValue(0)
                   .moveTo(g_trim)
                   ;
  trim_d_nbox = cp5.addNumberbox("Trimming Down")
                   .setLabel("Down")
                   .setPosition(80, 50)
                   .setWidth(60)
                   .setValue(0)
                   .moveTo(g_trim)
                   ;
                   
  cp5.addButton("Trimming Reset")
     .setLabel("Reset")
     .setPosition(220, 10)
     .moveTo(g_trim)
     .setColor(gcolor)
     .getCaptionLabel().align(CENTER, CENTER)
     ;
     
  cp5.addButton("Trimming Auto")
     .setLabel("Auto")
     .setPosition(220, 40)
     .moveTo(g_trim)
     .setColor(gcolor)
     .getCaptionLabel().align(CENTER, CENTER)
     ;
                   
  contrast_nbox = cp5.addNumberbox("Contrast")
                     .setLabel("Contrast")
                     .setPosition(10, 10)
                     .setWidth(60)
                     .setRange(-127, 127)
                     .setValue(0)
                     .setDirection(Controller.HORIZONTAL)
                     .moveTo(g_filter)
                     ;
  brightness_nbox = cp5.addNumberbox("Brightness")
                     .setLabel("Brightness")
                     .setPosition(100, 10)
                     .setWidth(60)
                     .setRange(-255, 255)
                     .setValue(0)
                     .setDirection(Controller.HORIZONTAL)
                     .moveTo(g_filter)
                     ;
  gray_cbox = cp5.addCheckBox("Gray")
                 .setPosition(200, 10)
                 .setSize(20,20)
                 .addItem("Grayscale", 0)
                 .moveTo(g_filter)
                 ;
  cp5.addButton("Apply Filter")
     .setLabel("Apply")
     .setPosition(50, 60)
     .moveTo(g_filter)
     .setColor(gcolor)
     .getCaptionLabel().align(CENTER, CENTER)
     ;
  cp5.addButton("Reset Filter")
     .setLabel("Reset")
     .setPosition(150, 60)
     .moveTo(g_filter)
     .setColor(gcolor)
     .getCaptionLabel().align(CENTER, CENTER)
     ;


                   
  
                   
  
  accordion = cp5.addAccordion("acc")
                 .setPosition(10,10)
                 .setWidth(guiwidth)
                 .addItem(g_file)
                 .setItemHeight(130)
                 .addItem(g_canvas)
                 .addItem(g_arrange)
                 .addItem(g_frame)
                 .addItem(g_grid)
                 .addItem(g_color)
                 .addItem(g_trim)
                 .addItem(g_filter)
                 ;
  accordion.open(0,1,2,3,4);
  accordion.setCollapseMode(Accordion.MULTI);
  cp5.loadProperties("setting.ser");
}


public void controlEvent(ControlEvent theEvent) {
  if(setup_finished && theEvent != null && theEvent.isController()) {
    //print(theEvent.controller().name() + "\n");
    if(theEvent.controller().name()=="< Prev"){
      il.prevImage();
    }
    else if(theEvent.controller().name()=="Next >"){
      il.nextImage();
    }
    else if(theEvent.controller().name()=="Open File"){
      selectInput("Select a file to process:", "fileSelected");
    }
    else if(theEvent.controller().name()=="Open Directory"){
      selectFolder("Select a folder to save:", "folderSelected_load");
    }
    else if(theEvent.controller().name()=="Close File"){
      il.deleteImage();
    }
    else if(theEvent.controller().name()=="Output Directory"){
      selectFolder("Select a folder to save:", "folderSelected");
    }
    
    else if(theEvent.controller().name()=="Generate"){
      if(separate_mode)cd.generate_sep();
      else cd.generate();
    }
    else if(theEvent.controller().name()=="Generate\nAll"){
      cd.generateAll();
    }
    else if(theEvent.controller().name()=="Swap") {
      int tmp = paperheight;
      paperheight = paperwidth;
      cheight_nbox.setValue(paperwidth);
      paperwidth = tmp;
      cwidth_nbox.setValue(tmp);
    }
    else if(theEvent.controller().name()=="Trimming Reset") {
      il.resetTrim();
    }
    else if(theEvent.controller().name()=="Trimming Auto") {
      il.autoTrim();
    }
    else if(theEvent.controller().name()=="Apply Filter") {
      il.filterImage();
    }
    else if(theEvent.controller().name()=="Reset Filter") {
      contrast_nbox.setValue(0.0f);
      brightness_nbox.setValue(0.0f);
      gray_cbox.deactivate(0);
      il.filterImage();
    }
    else {
      //nothing
    }
  }
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MoshaGen" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
