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
    lateinit var couch: Texture
    lateinit var coffeeTable: Texture
    lateinit var desk: Texture
    lateinit var deskChair: Texture
    lateinit var shelfCabinet: Texture
    lateinit var endTable: Texture

    override fun create() {
        batch = SpriteBatch()

        //TODO: change defaults
        floor = Texture("floor_default.png")
        couch = Texture("couch_lounge.png")
        bed = Texture("bed_double.png")
        coffeeTable = Texture("coffeetable_glass.png")
        desk = Texture("desk_plain.png")
        deskChair = Texture("deskchair_box.png")
        shelfCabinet = Texture("shelf_book.png")
        endTable = Texture("endtable_half.png")
    }

    override fun render() {
//        Gdx.gl.glClearColor(242f / 255F, 225f / 255F, 190f/255F, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        //TODO: change to equipped items
        this.drawFloor("")
        this.drawBed("")
        this.drawCouch("")
        this.drawCoffeeTable("")
        this.drawDesk("")
        this.drawDeskChair("")
        this.drawShelf("")
        this.drawEndTable("")
    }

    override fun dispose() {
        batch.dispose()
        floor.dispose()
        couch.dispose()
        bed.dispose()
        coffeeTable.dispose()
        desk.dispose()
        deskChair.dispose()
        shelfCabinet.dispose()
        endTable.dispose()
    }

    fun drawFloor(filename: String) {
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

    fun drawCouch(filename: String) {
        //TODO: change texture based on name

        batch.begin()
        val x = (Gdx.graphics.width - 4*floor.width) / 2f + 2*floor.width
        val y = (Gdx.graphics.height - 5*floor.height) / 2f + 5*2f

        batch.draw(couch, x, y)

        batch.end()
    }

    fun drawCoffeeTable(filename: String) {
        //TODO: change texture based on name

        batch.begin()
        val x = (Gdx.graphics.width - 5*floor.width) / 2f + 2*floor.width
        val y = (Gdx.graphics.height - 6*floor.height) / 2f + 6*2f

        batch.draw(coffeeTable, x, y)

        batch.end()
    }

    fun drawBed(filename: String) {
        //TODO: change texture based on name

        batch.begin()
        val x = (Gdx.graphics.width - 2*floor.width) / 2f + floor.width
        val y = (Gdx. graphics.height - floor.height) / 2f + 2f

        batch.draw(bed, x, y)
        batch.end()
    }

    fun drawDesk(filename: String) {
        //TODO: change texture based on name

        batch.begin()
        val x = (Gdx.graphics.width + 3*floor.width) / 2f - 4*floor.width
        val y = (Gdx. graphics.height - 2*floor.height) / 2f + 2*2f

        batch.draw(desk, x, y)
        batch.end()
    }

    fun drawDeskChair(filename:String) {
        //TODO: change texture based on name
        batch.begin()
        val x = (Gdx.graphics.width + 3*floor.width) / 2f - 3*floor.width
        val y = (Gdx. graphics.height - 2*floor.height) / 2f + 2*2f

        batch.draw(deskChair, x, y)
        batch.end()
    }

    fun drawShelf(filename: String) {
        //TODO: change texture based on name
        batch.begin()
        val x = (Gdx.graphics.width + 2*floor.width) / 2f - 2*floor.width + 75f
        val y = (Gdx. graphics.height + 2*floor.height) / 2f - 2*2f + 25f

        batch.draw(shelfCabinet, x, y)
        batch.end()
    }

    fun drawEndTable(filename: String) {
       //TODO: change texture based on name

        batch.begin()
        val x = (Gdx.graphics.width - floor.width) / 2f + 2*floor.width
        val y = (Gdx. graphics.height - floor.height) / 2f + 2f

        batch.draw(endTable, x, y)
        batch.end()
    }

}