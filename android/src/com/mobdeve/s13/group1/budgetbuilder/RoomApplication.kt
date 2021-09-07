package com.mobdeve.s13.group1.budgetbuilder

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils

class RoomApplication(): ApplicationAdapter(){
    lateinit var batch: SpriteBatch
    lateinit var block: Texture
    lateinit var chair: Texture
    lateinit var bed: Texture

    override fun create() {
        batch = SpriteBatch()
        block = Texture("floor.png")
        chair = Texture("loungechair_sw.png")
        bed = Texture("beddouble_se_scaled.png")
    }

    override fun render() {
        Gdx.gl.glClearColor(242f / 255F, 225f / 255F, 190f/255F, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        this.drawFloor()
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        block.dispose()
        chair.dispose()
        bed.dispose()
    }

    fun drawFloor() {

        //TODO: create formula after testing and move room to middle
        batch.draw(block, (5*block.width/2).toFloat() , (5*block.height/2 - 5*2f))
        batch.draw(block, (4*block.width/2).toFloat() , (4*block.height/2 - 4*2f))
        batch.draw(block, (3*block.width/2).toFloat() , (3*block.height/2 - 3*2f))
        batch.draw(block, (2*block.width/2).toFloat() , (2*block.height/2 - 2*2f))
        batch.draw(block, (block.width/2).toFloat() , (block.height/2 - 2f))
        batch.draw(block, 0f,0f)
        batch.draw(block, (4*block.width/2).toFloat()+block.width , (4*block.height/2 - 4*2f))
        batch.draw(block, (3*block.width/2).toFloat()+block.width , (3*block.height/2 - 3*2f))
        batch.draw(block, (2*block.width/2).toFloat()+block.width , (2*block.height/2 - 2*2f))
        batch.draw(block, (block.width/2).toFloat()+block.width , (block.height/2 - 2f))

        batch.draw(bed, (3*block.width/2).toFloat() , (2*block.height/2 - 2*2f))

        batch.draw(chair, (4*block.width/2).toFloat()+block.width , (4*block.height/2 - 4*2f))

//        batch.draw(block, ((Gdx.graphics.width - block.width) / 2).toFloat(), ((Gdx.graphics.height - block.height) / 2).toFloat())
    }
}