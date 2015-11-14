//ImageFileInfo.pde
//Description:Container of PImage and settings depend on it.
//Author:tiwawan
//Date:2015/3/26


import controlP5.*;

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
    color bcolor = pimg.pixels[0];
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
    float[] pix = {0.0, 0.0, 0.0, 0.0};
    float a = 255.0/(255.0-2*contrast);
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
