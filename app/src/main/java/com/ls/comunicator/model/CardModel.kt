package com.ls.comunicator.model

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.core.graphics.drawable.toBitmap
import com.ls.comunicator.core.getFilesDir
import java.io.*
import java.util.*


class CardModel {

    fun savePage(context: Context, page: String, card: Card?, callback: CompleteCallback) {
        SavePageCardsTask(context, callback).execute(page, card)
    }

    fun loadPage(context: Context, page: String?, callback: LoadPageCardsCallback) {
        LoadPageCardsTask(context, callback).execute(page)
    }

    fun loadCard(context: Context, page: String?, card: String?, callback: LoadCardCallback) {
        LoadCardTask(context, callback).execute(page, card)
    }

    fun deletePage(context: Context, page: String, card: String?, callback : CompleteCallback) {
        DeletePageTask(context, callback).execute(page, card)
    }

    fun renamePage(context: Context ,oldName: String, newName: String, callback : CompleteCallback) {
        RenamePageTask(context, callback).execute(oldName, newName)
    }

    fun loadPagesList(context: Context, callback: LoadPagesCallback) {
        LoadPagesListTask(context, callback).execute()
    }

    companion object {

        interface LoadPageCardsCallback {
            fun onLoad(cards: ArrayList<Card>?)
        }

        interface LoadCardCallback {
            fun onLoad(card: Card?)
        }

        interface LoadPagesCallback {
            fun onLoad(pages: ArrayList<String>?)
        }

        interface CompleteCallback {
            fun onComplete(result: Boolean)
        }

        internal class LoadPageCardsTask(val context: Context, private val callback: LoadPageCardsCallback?) :
            AsyncTask<Any , Void,  ArrayList<Card>>() {

            override fun doInBackground(vararg params: Any?): ArrayList<Card>? {
                val pageName = params[0] as String
                lateinit var fis: FileInputStream
                lateinit var ins: ObjectInputStream
                lateinit var listDir: File
                val cards = arrayListOf<Card>()
                val listDirPath = "/lists/${pageName.toLowerCase(Locale.getDefault())}"

                listDir = File(getFilesDir(context), listDirPath)

                try {
                    if (listDir.exists()) {
                        listDir.listFiles().forEach {
                            if (it.isDirectory) {
                                try {
                                    fis = FileInputStream(File("${it.absolutePath}/card_info"))
                                    ins = ObjectInputStream(fis)
                                    val card = ins.readObject() as Card
                                    card.page = pageName
                                    cards.add(card)

                                } catch (e: java.lang.Exception) {
                                } finally {
                                    ins.close()
                                    fis.close()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return cards
            }

            override fun onPostExecute(cards: ArrayList<Card>) {
                callback?.onLoad(cards)
            }

        }

        internal class LoadCardTask(val context: Context, private val callback: LoadCardCallback?) :
            AsyncTask<Any , Void,  Card>() {

            override fun doInBackground(vararg params: Any?): Card? {
                val page = params[0] as String
                val cardName = params[1] as String
                lateinit var fis: FileInputStream
                lateinit var ins: ObjectInputStream
                lateinit var listDir: File
                var card: Card? = null
                val listDirPath = "/lists/" +
                        "${page.toLowerCase(Locale.getDefault())}/" +
                        cardName.toLowerCase(Locale.getDefault())
                listDir = File(getFilesDir(context), listDirPath)
                try {
                    if (listDir.exists() && listDir.isDirectory) {
                        try {
                            fis = FileInputStream(File("${listDir.absolutePath}/card_info"))
                            ins = ObjectInputStream(fis)
                            card = ins.readObject() as Card
                            card.page = page
                        } catch (e: java.lang.Exception) {
                        } finally {
                            ins.close()
                            fis.close()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return card
            }

            override fun onPostExecute(card: Card) {
                callback?.onLoad(card)
            }
        }

        internal class LoadPagesListTask(val context: Context, private val callback: LoadPagesCallback?) :
            AsyncTask<Any , Void,  ArrayList<String>>() {

            override fun doInBackground(vararg params: Any?): ArrayList<String>? {
                lateinit var listsFolder: File
                val pagesList = ArrayList<String>()
                try {
                    listsFolder = File(getFilesDir(context), "/lists")
                    if (listsFolder.exists()) {
                        listsFolder.listFiles().forEach {
                            if (it.isDirectory)
                                pagesList.add(it.name)
                        }
                    }
                } catch (ex: Exception) { }
                return  pagesList
            }

            override fun onPostExecute(pages: ArrayList<String>) {
                callback?.onLoad(pages)
            }

        }

        internal class SavePageCardsTask(val context: Context, private val callback: CompleteCallback?) :
            AsyncTask<Any , Void,  Boolean>() {

            override fun doInBackground(vararg params: Any?): Boolean? {
                val listName = params[0] as String
                val card = params[1] as Card?
                lateinit var pageData: File
                lateinit var fos: FileOutputStream
                lateinit var os: ObjectOutputStream
                val pageDirPath  = "/lists/${listName.toLowerCase(Locale.getDefault())}"
                val cardDirPath = "$pageDirPath/${card?.name?.toLowerCase(Locale.getDefault())}"
                val cardSoundDirPath = "$cardDirPath/sound"
                val cardInfo = "card_info"
                val cardImage = "image.jpg"
                var success = true

                pageData = File(getFilesDir(context), pageDirPath)
                if (!pageData.exists()) success = pageData.mkdirs()
                    if (success && card != null) {
                        val cardDir =  File(getFilesDir(context), cardDirPath)
                        cardDir.mkdirs()
                        val cardInfoFile = File(cardDir, cardInfo) // save card_info
                        if (cardInfoFile.exists()) cardInfoFile.delete() else cardInfoFile.createNewFile()
                        try {
                            fos = FileOutputStream(cardInfoFile)
                            os = ObjectOutputStream(fos)
                            os.writeObject(card)
                        } catch (e : Exception) {
                            success = false
                            e.printStackTrace()
                        } finally {
                            os.flush()
                            os.close()
                            fos.close()
                        }
                        val cardImageFile = File(cardDir, cardImage) // save card image
                        if (cardImageFile.exists()) cardImageFile.delete()
                        try {
                            fos = FileOutputStream(cardImageFile)
                            card.image?.imageView?.drawable?.toBitmap()
                                ?.compress(Bitmap.CompressFormat.PNG, 50, fos)
                        } catch (e : Exception) {
                            success = false
                            e.printStackTrace()
                        } finally {
                            fos.close()
                        }
                        File(getFilesDir(context), cardSoundDirPath).mkdirs()
                    }
                return success
            }

            override fun onPostExecute(result: Boolean) {
                callback?.onComplete(result)
            }
        }

        internal class DeletePageTask(val context: Context, private val callback: CompleteCallback?) :
            AsyncTask<Any , Void,  Boolean>() {

            override fun doInBackground(vararg params: Any?): Boolean? {
                val page = params[0] as String
                val card = params[1] as String?
                lateinit var pageData: File
                lateinit var path: String
                return try {
                    path = if (card == null)
                        "/lists/${page.toLowerCase(Locale.getDefault())}"
                    else
                        "/lists/${page.toLowerCase(Locale.getDefault())}/${card.toLowerCase(Locale.getDefault())}"
                    pageData = File(getFilesDir(context), path)

                    pageData.deleteRecursively()
                    (!pageData.exists())
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }

            override fun onPostExecute(result: Boolean) {
                callback?.onComplete(result)
            }

        }

        internal class RenamePageTask(val context: Context, private val callback: CompleteCallback?) :
            AsyncTask<Any , Void,  Boolean>() {

            override fun doInBackground(vararg params: Any?): Boolean? {
                val oldName = params[0] as String
                val newName = params[1] as String
                lateinit var oldPage: File
                lateinit var newPage: File
                return try {
                    val oldPath = "/lists/${oldName.toLowerCase(Locale.getDefault())}"
                    val newPath = "/lists/${newName.toLowerCase(Locale.getDefault())}"
                    val files = getFilesDir(context)
                    oldPage = File(files, oldPath)
                    newPage = File(files, newPath)
                    oldPage.renameTo(newPage)
                } catch (ex: Exception) {
                    false
                }
            }

            override fun onPostExecute(result: Boolean) {
                callback?.onComplete(result)
            }

        }
    }
}