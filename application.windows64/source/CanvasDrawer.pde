//CanvasDrawer.pde
//Description:Class to render in canvas and image
//Author:tiwawan
//Date:2015/3/26

class CanvasDrawer {
  
  ImageDrawer id;
  
  public CanvasDrawer(ImageDrawer idraw) {
    id = idraw;
  }
  
  void drawCanvas(PGraphics[] graphs) {
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
  
  void drawCanvas_sep(PGraphics[] graphs) {
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
  
  
  String elimExt(String str) {
    int dot = str.indexOf(".");
    if(dot>0)return str.substring(0, dot);
    else return str;
  }
  
  String getExt(String str) {
    int dot = str.indexOf(".");
    if(dot>0)return str.substring(dot, str.length());
    else return "";
  }
  
  void generate() {
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
  
  void generate_sep() {
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
  
  void generateAll() {
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
