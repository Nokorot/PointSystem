/*
 *  ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * ==================================================================== 
 */

package org.apache.poi.xslf.usermodel;

import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.sl.usermodel.Placeholder;

/**
 * How to set slide title
 */
public class Tutorial3 {

    public static void main(String[] args) throws IOException{
        XMLSlideShow ppt = new XMLSlideShow();

        try {
            XSLFSlide slide = ppt.createSlide();
    
            XSLFTextShape titleShape = slide.createTextBox();
            titleShape.setPlaceholder(Placeholder.TITLE);
            titleShape.setText("This is a slide title");
            titleShape.setAnchor(new Rectangle(50, 50, 400, 100));
    
            FileOutputStream out = new FileOutputStream("title.pptx");
            ppt.write(out);
            out.close();
        } finally {
            ppt.close();
        }
    }
}
