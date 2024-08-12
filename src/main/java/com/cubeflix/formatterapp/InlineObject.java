/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;

/**
 *
 * @author Kevin Chen
 */
public interface InlineObject {
    public float getWidth() throws IOException;
    public float getHeight() throws IOException;
    public boolean isVisible();
    public boolean forceWordBreak();
}
