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


void gui() {
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

  CColor opcolor = new CColor(#B21B85, #6F0D52, #B21B85, #FFFFFF, #FFFFFF);

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
     
  CColor gcolor = new CColor(#9DE815, #15B202, #9DE815, #FFFFFF, #FFFFFF);
  
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
                   .setRange(1.0, 20000.0)
                   .setValue(3564.0)
                   .updateDisplayMode(1)
                   .moveTo(g_canvas)
                   ;
                   
  cheight_nbox = cp5.addNumberbox("Height")
                    .setPosition(10, 50)
                    .setLabel("Height(px)")
                    .setSize(100, 20)
                    .setRange(1.0, 20000.0)
                    .setValue(2520.0)
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
                   .setRange(0.1,4.0)
                   .setValue(2.0)
                   .setScrollSensitivity(0.1)
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
                   .moveTo(g_arrange)
                   ;
  space_nbox = cp5.addNumberbox("Space")
                   .setPosition(200, 50)
                   .setSize(60, 20)
                   .setRange(1,1000)
                   .setValue(100)
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
                         .setRange(0,20.0)
                         .setValue(5.0)
                         .setScrollSensitivity(0.1)
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
                               .setValue(3.0)
                               .setScrollSensitivity(0.1)
                               .setSliderMode(Slider.FIX)
                               .setNumberOfTickMarks(16)
                               .moveTo(g_grid)
                               ;
  grid_vertical_slider = cp5.addSlider("Grid Vertical")
                             .setLabel("Vertical")
                             .setPosition(80,40)
                             .setSize(140,18)
                             .setRange(0,15)
                             .setValue(3.0)
                             .setScrollSensitivity(0.1)
                             .setSliderMode(Slider.FIX)
                             .setNumberOfTickMarks(16)
                             .moveTo(g_grid)
                             ;
  grid_thickness_slider = cp5.addSlider("Grid Thickness")
                             .setLabel("Thickness")
                             .setPosition(80,70)
                             .setSize(140,18)
                             .setRange(0,20)
                             .setValue(4.0)
                             .setScrollSensitivity(0.1)
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
                     .setRange(-128, 128)
                     .setValue(0)
                     .moveTo(g_filter)
                     ;
  brightness_nbox = cp5.addNumberbox("Brightness")
                     .setLabel("Brightness")
                     .setPosition(100, 10)
                     .setWidth(60)
                     .setRange(-255, 255)
                     .setValue(0)
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
     .setPosition(100, 60)
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


void controlEvent(ControlEvent theEvent) {
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
    else {
      //nothing
    }
  }
}



