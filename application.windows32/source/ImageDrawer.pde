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

  void updateParameters(){
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
    if(arrangemode == 0 && mgnf>1.0) {
      is_image_oblong = (float)paperwidth/paperheight < ((float)(1+mgnf)*trimwidth/(mgnf*trimheight));
    }
    else if(arrangemode == 0) {
      is_image_oblong = (float)paperwidth/paperheight < ((float)(1+mgnf)*trimwidth/(trimheight));
    }
    else if(arrangemode == 1 && mgnf>1.0) {
      is_image_oblong = (float)paperwidth/paperheight < ((float)mgnf*trimwidth/((1+mgnf)*trimheight));
    }
    else {
      is_image_oblong = (float)paperwidth/paperheight < ((float)trimwidth/((1+mgnf)*trimheight));
    }
  
    if(arrangemode == 0 && is_image_oblong) {
      w_s = (paperwidth-2*margin-space)/(mgnf+1.0);
      w_l = mgnf*(paperwidth-2*margin-space)/(mgnf+1.0);
      h_s = trimheight*w_s/trimwidth;
      h_l = trimheight*w_l/trimwidth;
    }
    else if(arrangemode == 1 && !is_image_oblong){
      h_s = (paperheight-2*margin-space)/(mgnf+1.0);
      h_l = mgnf*(paperheight-2*margin-space)/(mgnf+1.0);
      w_l = trimwidth*h_l/trimheight;
      w_s = trimwidth*h_s/trimheight;
    }
    else if(arrangemode == 0 && !is_image_oblong){
      if(mgnf > 1.0) {
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
      if(mgnf > 1.0) {
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
        y_s = (float)(paperheight-h_s)/2.0;
        y_l = (float)(paperheight-h_l)/2.0;
      }
    }
    else if(arrangemode == 1){
      x_s = margin;
      y_s = margin;
      x_l = margin;
      y_l = margin+space+h_s;
      if(centering) {
        x_s = (float)(paperwidth-w_s)/2.0;
        x_l = (float)(paperwidth-w_l)/2.0;
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

  void updateParameters_sep() {
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
      if(mgnf >= 1.0){
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
      if(mgnf >= 1.0) {
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

  void updateGuiParams() {
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
  float prev_x(float p) {
    p = prev_s(p);
    p += guiwidth+prevmargin_w;
    return p;
  }

  // scale and translate y position to preview space
  float prev_y(float p) {
    p = prev_s(p);
    p += prevmargin_h;
    return p;
  }

  // scale to preview space
  float prev_s(float p) {
    if((float)prevwidth/prevheight < (float)paperwidth/paperheight) p = p * (float)prevwidth/paperwidth;
    else p = p * (float)prevheight/paperheight;
    if(p>=0)return p;
    else return -p;
  }

  void image_prev(PImage im, float x, float y, float w, float h, int u1, int v1, int u2, int v2) {
    image(im, prev_x(x), prev_y(y), prev_s(w), prev_s(h), u1, v1, u2, v2);
  }

  void rect_prev(float x, float y, float w, float h) {
    rect(prev_x(x), prev_y(y), prev_s(w), prev_s(h));
  }

  void rect_prev_r(float x, float y, float w, float h, float r) {
    rect(prev_x(x), prev_y(y), prev_s(w), prev_s(h), prev_s(r));
  }

  void line_prev(float x1, float y1, float x2, float y2) {
    line(prev_x(x1), prev_y(y1), prev_x(x2), prev_y(y2));
  }

  void strokeWeight_prev(float t) {
    strokeWeight(prev_s(t));
  }
  
  float prev_x_1(float p) {
    p = prev_s_sep(p);
    p += guiwidth+prevmargin_w;
    return p;
  }

  // scale and translate y position to preview space
  float prev_y_1(float p) {
    p = prev_s_sep(p);
    p += prevmargin_h;
    return p;
  }

  float prev_x_2(float p) {
    p = prev_s_sep(p);
    p += guiwidth+prev_s_sep(paperwidth)+prevspace+prevmargin_w;
    return p;
  }

  // scale and translate y position to preview space
  float prev_y_2(float p) {
    p = prev_s_sep(p);
    p += prevmargin_h;
    return p;
  }

  // scale to preview space
  float prev_s_sep(float p) {
    if((float)prevwidth/prevheight < (float)(paperwidth*2)/paperheight) p = p * (float)prevwidth/(paperwidth*2);
    else p= p * (float)prevheight/paperheight;
    //p = p * ((float)prevwidth/(paperwidth*2));
    if(p>=0)return p;
    else return -p;
  }

  void image_prev_1(PImage im, float x, float y, float w, float h, int u1, int v1, int u2, int v2) {
    image(im, prev_x_1(x), prev_y_1(y), prev_s_sep(w), prev_s_sep(h), u1, v1, u2, v2);
  }

  void image_prev_2(PImage im, float x, float y, float w, float h, int u1, int v1, int u2, int v2) {
    image(im, prev_x_2(x), prev_y_2(y), prev_s_sep(w), prev_s_sep(h), u1, v1, u2, v2);
  }

  void rect_prev_1(float x, float y, float w, float h) {
    rect(prev_x_1(x), prev_y_1(y), prev_s_sep(w), prev_s_sep(h));
  }

  void rect_prev_2(float x, float y, float w, float h) {
    rect(prev_x_2(x), prev_y_2(y), prev_s_sep(w), prev_s_sep(h));
  }

  void rect_prev_r_1(float x, float y, float w, float h, float r) {
    rect(prev_x_1(x), prev_y_1(y), prev_s_sep(w), prev_s_sep(h), prev_s_sep(r));
  }

  void rect_prev_r_2(float x, float y, float w, float h, float r) {
    rect(prev_x_1(x), prev_y_1(y), prev_s_sep(w), prev_s_sep(h), prev_s_sep(r));
  }

  void line_prev_1(float x1, float y1, float x2, float y2) {
    line(prev_x_1(x1), prev_y_1(y1), prev_x_1(x2), prev_y_1(y2));
  }

  void line_prev_2(float x1, float y1, float x2, float y2) {
    line(prev_x_2(x1), prev_y_2(y1), prev_x_2(x2), prev_y_2(y2));
  }

  void strokeWeight_prev_sep(float t) {
    strokeWeight(prev_s_sep(t));
  }
  
  void image_swtch(int target, PImage im, float x, float y, float w, float h, int u1, int v1, int u2, int v2, PGraphics graph) {
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

  void rect_swtch(int target, float x, float y, float w, float h, PGraphics graph) {
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

  void line_swtch(int target, float x1, float y1, float x2, float y2, PGraphics graph) {
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

  void fill_swtch(color c, PGraphics graph) {
    if(graph == null) fill(c);
    else graph.fill(c);
  }

  void strokeWeight_swtch(int target, float t, PGraphics graph) {
    if(graph == null){
      if(target == 0)strokeWeight_prev(t);
      else strokeWeight_prev_sep(t);
    }
    else graph.strokeWeight(t);
  }

  void stroke_swtch(color c, PGraphics graph) {
    if(graph == null) stroke(c);
    else graph.stroke(c);
  }

  void noStroke_swtch(PGraphics graph) {
    if(graph == null) noStroke();
    else graph.noStroke();
  }
  
  void drawFrame_swtch(int target, float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2, PGraphics g) {
    if(frame_w == 1 && !(arrangemode==0 && extend && target==0) ){
      rect_swtch(target, x1-thickness*frame_h, y1-thickness, w1+2*thickness*frame_h, thickness, g);
      rect_swtch(target, x1-thickness*frame_h, y1+h1, w1+2*thickness*frame_h, thickness, g);
    }
    if(frame_h == 1 && !(arrangemode==1 && extend && target==0) ){
      rect_swtch(target, x2-thickness, y2-thickness*frame_w, thickness, h2+2*thickness*frame_w, g);
      rect_swtch(target, x2+w2, y2-thickness*frame_w, thickness, h2+2*thickness*frame_w, g);
    }
  }

  void drawGrid_swtch(int target, float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2, PGraphics g) {
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
  
  void drawSquareGrid_swtch(int target,  float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2, float size, PGraphics g) {
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
  
  void drawBackground(PGraphics pg) {
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
  void drawFrame(int size, PGraphics pg) {
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
  
  void drawGrid(int size, PGraphics pg) {
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
  
  void drawSquareGrid(int size, PGraphics pg) {
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
  
  void drawImage(int size, PGraphics pg) {
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
