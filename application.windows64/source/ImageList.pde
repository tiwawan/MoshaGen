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
  
  void firstImage() {
    if(!image_loaded)return;
    image_selected_index = 0;
    info_selected = images.get(image_selected_index);
    image_selected = info_selected.pimg;
    fcursor_tarea.setText("" + (image_selected_index+1) + "/" + images.size());
    setTrimGui();
    setFilter();
  }

  void nextImage() {
    if(!image_loaded || image_selected_index == images.size()-1)return;
    image_selected_index++;
    info_selected = images.get(image_selected_index);
    image_selected = info_selected.pimg;
    fcursor_tarea.setText("" + (image_selected_index+1) + "/" + images.size());
    setTrimGui();
    setFilter();
  }

  void prevImage() {
    if(!image_loaded || image_selected_index == 0)return;
    image_selected_index--;
    info_selected = images.get(image_selected_index);
    image_selected = info_selected.pimg;
    fcursor_tarea.setText("" + (image_selected_index+1) + "/" + images.size());
    setTrimGui();
    setFilter();
  }
  
  ImageFileInfo getCurrentInfo() {
    return info_selected;
  }
  
  PImage getCurrentImage() {
    return image_selected;
  }
  
  void setTrimGui() {
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
