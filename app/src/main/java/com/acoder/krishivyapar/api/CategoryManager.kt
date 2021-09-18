package com.acoder.krishivyapar.api

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class CategoryManager(
    context: Context,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private var sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Category343Database"
        private const val PREF_NAME = "Category343Pref"
        private var CATEGORY_TABLE = "categories"
        private var SUB_CATEGORY_TABLE = "sub_categories"


    }
    data class CategoryModel(
        val categoryId: Int,
        val json: JSONObject,
        val status: Int,
        val position: Int
    )

    data class SubCategoryModel(
        val subCategoryId: Int,
        val categoryId: Int,
        val json: JSONObject,
        val status: Int,
        val position: Int
    )

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE IF NOT EXISTS $CATEGORY_TABLE (id INTEGER PRIMARY KEY AUTOINCREMENT, category_id INTEGER, json TEXT, status INTEGER, position INTEGER);")
        db.execSQL("CREATE TABLE IF NOT EXISTS $SUB_CATEGORY_TABLE (id INTEGER PRIMARY KEY AUTOINCREMENT, sub_category_id INTEGER, category_id INTEGER, json TEXT, status INTEGER, position INTEGER);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun initializeCategories(version: Int, categoryJArr: JSONArray) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $CATEGORY_TABLE")
        for (i in 0 until categoryJArr.length()) {
            val categoryObject = categoryJArr.getJSONObject(i)
            val contentValues = ContentValues()
            contentValues.put("category_id", categoryObject.getInt("category_id"))
            contentValues.put("json", categoryObject.getJSONObject("json").toString())
            contentValues.put("position", categoryObject.getInt("position"))
            contentValues.put("status", categoryObject.getInt("status"))
            db.insert(CATEGORY_TABLE, null, contentValues)
        }
        updateVersion(CATEGORY_TABLE, version)
    }

    fun initializeSubCategories(version: Int, subCategoryJArr: JSONArray) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $SUB_CATEGORY_TABLE")
        for (i in 0 until subCategoryJArr.length()) {
            val subCategoryObject = subCategoryJArr.getJSONObject(i)
            val contentValues = ContentValues()
            contentValues.put("sub_category_id", subCategoryObject.getInt("sub_category_id"))
            contentValues.put("category_id", subCategoryObject.getInt("category_id"))
            contentValues.put("json", subCategoryObject.getJSONObject("json").toString())
            contentValues.put("position", subCategoryObject.getInt("position"))
            contentValues.put("status", subCategoryObject.getInt("status"))
            db.insert(SUB_CATEGORY_TABLE, null, contentValues)
            Log.d("insert", "$i")
        }
        updateVersion(SUB_CATEGORY_TABLE, version)
    }

    fun getCategories(): List<CategoryModel> {
        val db = this.writableDatabase
        val categoryList = ArrayList<CategoryModel>()
        val c = db.rawQuery(
            "SELECT category_id, json, status, position FROM $CATEGORY_TABLE ORDER BY position",
            null
        )
        while (c.moveToNext()) {
            val json = JSONObject(c.getString(1))
            categoryList.add(CategoryModel(c.getInt(0), json, c.getInt(2), c.getInt(3)))
        }
        c.close()
        return categoryList
    }

    fun getSubCategories(categoryId: Int): List<SubCategoryModel> {
        val db = this.writableDatabase
        val categoryList = ArrayList<SubCategoryModel>()
        val c = db.rawQuery(
            "SELECT sub_category_id, category_id, json, status, position FROM $SUB_CATEGORY_TABLE WHERE category_id = $categoryId ORDER BY position",
            null
        )
        while (c.moveToNext()) {
            val json = JSONObject(c.getString(2))
            categoryList.add(
                SubCategoryModel(
                    c.getInt(0), c.getInt(1), json, c.getInt(3), c.getInt(4)
                )
            )
            Log.d("get", "*****")
        }
        c.close()
        return categoryList
    }

    fun getCategoryVersion(): Int {
        return sharedPref.getInt(CATEGORY_TABLE, 0)
    }

    fun getSubCategoryVersion(): Int {
        return sharedPref.getInt(SUB_CATEGORY_TABLE, 0)
    }

    private fun updateVersion(table: String, version: Int) {
        val editor = sharedPref.edit()
        editor.putInt(table, version)
        editor.apply()
    }


}