package com.mobdeve.s13.group1.budgetbuilder

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class RoomApplication: ApplicationAdapter(){
    val ROOM_SIZE = 6

    lateinit var batch: SpriteBatch
    lateinit var floor: Texture
    lateinit var bed: Texture
    lateinit var chair: Texture

    override fun create() {
        batch = SpriteBatch()
        floor = Texture("floor.png")

        //TODO: change defaults
        chair = Texture("loungechair.png")
        bed = Texture("beddouble.png")
    }

    override fun render() {
//        Gdx.gl.glClearColor(242f / 255F, 225f / 255F, 190f/255F, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        this.drawFloor()
        this.drawChair("")
        this.drawBed("")
    }

    override fun dispose() {
        batch.dispose()
        floor.dispose()
        chair.dispose()
        bed.dispose()
    }

    fun drawFloor() {
        batch.begin()
        var x: Float
        var y: Float

        for(j in 0 until ROOM_SIZE) {
            for(i in (j-3) until (ROOM_SIZE+(j-4)) + 1){
                x = (Gdx.graphics.width - i*floor.width) / 2f - (2-j)*floor.width
                y = (Gdx.graphics.height - i*floor.height) / 2f + i*2f
                batch.draw(floor, x, y)
            }
        }

        batch.end()
    }

    fun drawChair(name: String) {
        //TODO: change texture based on name

        batch.begin()
        val x = (Gdx.graphics.width - 4*floor.width) / 2f + 2*floor.width
        val y = (Gdx.graphics.height - 4*floor.height) / 2f + 4*2f

        batch.draw(chair, x, y)

        batch.end()
    }

    fun drawBed(name: String) {
        //TODO: change texture based on name

        batch.begin()
        val x = (Gdx.graphics.width - 2*floor.width) / 2f + floor.width
        val y = (Gdx. graphics.height - floor.height) / 2f + 2f

        batch.draw(bed, x, y)
        batch.end()
    }

}