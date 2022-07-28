/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.csi.iuffi.iuffiweb.util.pdf;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

/**
 * Example of a custom PDFGraphicsStreamEngine subclass. Allows text and
 * graphics to be processed in a custom manner. This example simply prints the
 * operations to stdout.
 *
 * <p>
 * See {@link PDFStreamEngine} for further methods which may be overridden.
 * 
 * @author John Hewson
 */
public class CustomGraphicsStreamEngine extends PDFGraphicsStreamEngine
{
  public List<IValuableElement> valuableElements = new ArrayList<IValuableElement>();
  public int                    pageNumber       = 0;

  /**
   * Constructor.
   *
   * @param page
   *          PDF Page
   * @param pageCounter2
   */
  protected CustomGraphicsStreamEngine(PDPage page, int pageNumber)
  {
    super(page);
    this.pageNumber = pageNumber;
  }

  /**
   * Runs the engine on the current page.
   *
   * @throws IOException
   *           If there is an IO error while drawing the page.
   */
  public void run() throws IOException
  {
    processPage(getPage());

    for (PDAnnotation annotation : getPage().getAnnotations())
    {
      showAnnotation(annotation);
    }
  }

  @Override
  public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3)
      throws IOException
  {
    valuableElements.add(new PDFRectangle(p0, p1, p2, p3, pageNumber));
  }

  /**
   * Overridden from PDFStreamEngine.
   */
  @Override
  public void showTextString(byte[] string) throws IOException
  {
    valuableElements.add(new PDFText(new String(string)));
    super.showTextString(string);
  }

  public void drawImage(PDImage pdImage) throws IOException
  {

  }

  @Override
  public void clip(int windingRule) throws IOException
  {
  }

  @Override
  public void moveTo(float x, float y) throws IOException
  {
  }

  @Override
  public void lineTo(float x, float y) throws IOException
  {
  }

  @Override
  public void curveTo(float x1, float y1, float x2, float y2, float x3,
      float y3) throws IOException
  {
    valuableElements
        .add(new PDFCurvedRectangle(x1, y1, x2, y2, x3, y3, pageNumber));
  }

  @Override
  public Point2D getCurrentPoint() throws IOException
  {
    return new Point2D.Float(0, 0);
  }

  @Override
  public void closePath() throws IOException
  {
  }

  @Override
  public void endPath() throws IOException
  {
  }

  @Override
  public void strokePath() throws IOException
  {
  }

  @Override
  public void fillPath(int windingRule) throws IOException
  {
  }

  @Override
  public void fillAndStrokePath(int windingRule) throws IOException
  {
  }

  @Override
  public void shadingFill(COSName shadingName) throws IOException
  {
  }

  /**
   * Overridden from PDFStreamEngine.
   */
  @Override
  public void showTextStrings(COSArray array) throws IOException
  {
    super.showTextStrings(array);
  }

  /**
   * Overridden from PDFStreamEngine.
   */
  @Override
  protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code,
      String unicode,
      Vector displacement) throws IOException
  {
    super.showGlyph(textRenderingMatrix, font, code, unicode, displacement);
  }

  public List<IValuableElement> getValuableElements()
  {
    return valuableElements;
  }

  public void setValuableElements(List<IValuableElement> valuableElements)
  {
    this.valuableElements = valuableElements;
  }

  public int getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber)
  {
    this.pageNumber = pageNumber;
  }
}
