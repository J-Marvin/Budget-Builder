package com.mobdeve.s13.group1.budgetbuilder

import android.util.Log
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.ScreenUtils
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureModel
import java.util.concurrent.Executors


class RoomApplication(var data: ArrayList<FurnitureModel>): ApplicationAdapter(){
    val ROOM_SIZE = 6
    val FLOOR_WIDTH = 150
    val FLOOR_HEIGHT = 110

    lateinit var batch: SpriteBatch
    lateinit var equipped: HashMap<String, Texture>
    var exporting: Boolean = false
    var path: String? = null

    override fun create() {
        batch = SpriteBatch()
        equipped = HashMap()
        for(furniture in data) {
            equipped[furniture.type] = Texture(furniture.roompath)
        }
    }

    override fun render() {
//        Gdx.gl.glClearColor(242f / 255F, 225f / 255F, 190f/255F, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        drawFloor(equipped["floor"])
        drawBed(equipped["bed"])
        drawCouch(equipped["couch"])
        drawCoffeeTable(equipped["coffeetable"])
        drawDesk(equipped["desk"])
        drawDeskChair(equipped["chair"])
        drawShelf(equipped["shelf"])
        drawEndTable(equipped["endtable"])

        batch.end()

        if (exporting) {
            val pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight, true)


            val pixmap = Pixmap(
                Gdx.graphics.backBufferWidth,
                Gdx.graphics.backBufferHeight,
                Pixmap.Format.RGBA8888
            )
            BufferUtils.copy(pixels, 0, pixmap.pixels, pixels.size)
            PixmapIO.writePNG(Gdx.files.local(path), pixmap)
            pixmap.dispose()
            exporting = false
        }
    }

    override fun dispose() {
        batch.dispose()
        for(furniture in equipped) {
            furniture.value.dispose()
        }
    }

    fun drawFloor(floor: Texture?) {
        if(floor != null) {
            var x: Float
            var y: Float

            for(j in 0 until ROOM_SIZE) {
                for(i in (j-3) until (ROOM_SIZE+(j-4)) + 1){
                    x = (Gdx.graphics.width - i*FLOOR_WIDTH) / 2f - (2-j)*FLOOR_WIDTH
                    y = (Gdx.graphics.height - i*FLOOR_HEIGHT) / 2f + i*2f
                    batch.draw(floor, x, y)
                }
            }
        }
    }

    fun drawCouch(couch: Texture?) {
        if(couch != null) {
            val x = (Gdx.graphics.width - 4*FLOOR_WIDTH) / 2f + 2*FLOOR_WIDTH
            val y = (Gdx.graphics.height - 5*FLOOR_HEIGHT) / 2f + 5*2f

            batch.draw(couch, x, y)
        }
    }

    fun drawCoffeeTable(coffeeTable: Texture?) {
        if(coffeeTable != null) {
            val x = (Gdx.graphics.width - 5*FLOOR_WIDTH) / 2f + 2*FLOOR_WIDTH
            val y = (Gdx.graphics.height - 6*FLOOR_HEIGHT) / 2f + 6*2f

            batch.draw(coffeeTable, x, y)
        }
    }

    fun drawBed(bed: Texture?) {
        if(bed != null) {
            val x = (Gdx.graphics.width - 2*FLOOR_WIDTH) / 2f + FLOOR_WIDTH
            val y = (Gdx. graphics.height - FLOOR_HEIGHT) / 2f + 2f

            batch.draw(bed, x, y)
        }
    }

    fun drawDesk(desk: Texture?) {
        if(desk != null) {
            val x = (Gdx.graphics.width + 3*FLOOR_WIDTH) / 2f - 4*FLOOR_WIDTH
            val y = (Gdx. graphics.height - 2*FLOOR_HEIGHT) / 2f + 2*2f

            batch.draw(desk, x, y)
        }
    }

    fun drawDeskChair(deskChair: Texture?) {
        if(deskChair != null) {
            val x = (Gdx.graphics.width + 3*FLOOR_WIDTH) / 2f - 3*FLOOR_WIDTH
            val y = (Gdx. graphics.height - 2*FLOOR_HEIGHT) / 2f + 2*2f

            batch.draw(deskChair, x, y)
        }
    }

    fun drawShelf(shelfCabinet: Texture?) {
        if(shelfCabinet != null) {
            val x = (Gdx.graphics.width + 2*FLOOR_WIDTH) / 2f - 2*FLOOR_WIDTH + 75f
            val y = (Gdx. graphics.height + 2*FLOOR_HEIGHT) / 2f - 2*2f + 25f

            batch.draw(shelfCabinet, x, y)
        }
    }

    fun drawEndTable(endTable: Texture?) {
        if(endTable != null) {
            val x = (Gdx.graphics.width - FLOOR_WIDTH) / 2f + 2*FLOOR_WIDTH
            val y = (Gdx. graphics.height - FLOOR_HEIGHT) / 2f + 2f

            batch.draw(endTable, x, y)
        }
    }

}