package com.mobdeve.s13.group1.budgetbuilder

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.utils.ScreenUtils

class RoomApplication(): ApplicationAdapter(){
    lateinit var batch: SpriteBatch
    lateinit var img: Texture
    lateinit var tileMap: TiledMap

    override fun create() {
        batch = SpriteBatch()
        img = Texture("temp_room.png")
    }

    override fun render() {
        Gdx.gl.glClearColor(242f / 255F, 225f / 255F, 190f/255F, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin()
        batch.draw(img, ((Gdx.graphics.width - img.width) / 2).toFloat(), ((Gdx.graphics.height - img.height) / 2 - (Gdx.graphics.height * .1)).toFloat())
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
//        tileMap.dispose()
    }
}