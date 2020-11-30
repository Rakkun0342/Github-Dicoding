package com.ansel.githubdicoding

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ansel.githubdicoding.adapter.SectionsPagerAdapter
import com.ansel.githubdicoding.db.DatabaseContract.FavColumns.Companion.AVATAR
import com.ansel.githubdicoding.db.DatabaseContract.FavColumns.Companion.COMPANY
import com.ansel.githubdicoding.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.ansel.githubdicoding.db.DatabaseContract.FavColumns.Companion.FAVORITE
import com.ansel.githubdicoding.db.DatabaseContract.FavColumns.Companion.FOLLOWERS
import com.ansel.githubdicoding.db.DatabaseContract.FavColumns.Companion.FOLLOWING
import com.ansel.githubdicoding.db.DatabaseContract.FavColumns.Companion.LOCATION
import com.ansel.githubdicoding.db.DatabaseContract.FavColumns.Companion.NAME
import com.ansel.githubdicoding.db.DatabaseContract.FavColumns.Companion.REPOSITORY
import com.ansel.githubdicoding.db.DatabaseContract.FavColumns.Companion.USERNAME
import com.ansel.githubdicoding.db.FavoriteHelper
import com.ansel.githubdicoding.entity.Favorite
import com.ansel.githubdicoding.entity.User
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val UserList = "UserList"
        const val FavoriteList = "UserList"
        const val PositionList = "PositionList"
        const val NoteList = "NoteList"
    }

    private var isFavorite = false
    private lateinit var gitHelper: FavoriteHelper
    private var favorites: Favorite? = null
    private lateinit var imageAvatar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        gitHelper = FavoriteHelper.getInstance(applicationContext)
        gitHelper.open()

        favorites = intent.getParcelableExtra(NoteList)
        if (favorites != null) {
            setDataObject()
            isFavorite = true
            val checked: Int = R.drawable.favorite
            fab_add.setImageResource(checked)
        } else {
            setData()
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        fab_add.setOnClickListener(this)
    }

    private fun setData() {
        val user = intent.getParcelableExtra<User>(UserList) as User

        user.name?.let { setActionBarTitle(it) }

        Glide.with(this)
            .load(user.avatar)
            .into(img_detail_photo)

        val textUsername = "github.com/${user.username}"

        tv_detail_name.text = user.name
        tv_detail_username.text = textUsername
        tv_detail_company.text = user.company
        tv_detail_location.text = user.location
        tv_detail_repository.text = user.repository
        tv_detail_following.text = user.following
        tv_detail_followers.text = user.followers

        imageAvatar = user.avatar.toString()
    }

    private fun setDataObject() {
        val favoriteUser = intent.getParcelableExtra<Favorite>(NoteList) as Favorite

        favoriteUser.name?.let { setActionBarTitle(it) }

        Glide.with(this)
            .load(favoriteUser.avatar)
            .into(img_detail_photo)

        val textUsername = "github.com/${favoriteUser.username}"

        tv_detail_name.text = favoriteUser.name
        tv_detail_username.text = textUsername
        tv_detail_company.text = favoriteUser.company
        tv_detail_location.text = favoriteUser.location
        tv_detail_repository.text = favoriteUser.repository
        tv_detail_following.text = favoriteUser.following
        tv_detail_followers.text = favoriteUser.followers

        imageAvatar = favoriteUser.avatar.toString()
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    override fun onClick(view: View) {
        val checked: Int = R.drawable.favorite
        val unChecked: Int = R.drawable.favorite_border
        if (view.id == R.id.fab_add) {
            if (isFavorite) {
                gitHelper.deleteById(favorites?.username.toString())
                Toast.makeText(this, getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show()
                fab_add.setImageResource(unChecked)
                isFavorite = false
            } else {
                val dataUsername = tv_detail_username.text.toString()
                val dataName = tv_detail_name.text.toString()
                val dataAvatar = imageAvatar
                val datacompany = tv_detail_company.text.toString()
                val dataLocation = tv_detail_location.text.toString()
                val dataRepository = tv_detail_repository.text.toString()
                val dataFollowing = tv_detail_following.text.toString()
                val dataFollowers = tv_detail_followers.text.toString()
                val dataFavorite = "1"

                val values = ContentValues()
                values.put(USERNAME, dataUsername)
                values.put(NAME, dataName)
                values.put(COMPANY, datacompany)
                values.put(LOCATION, dataLocation)
                values.put(REPOSITORY, dataRepository)
                values.put(FOLLOWING, dataFollowing)
                values.put(FOLLOWERS, dataFollowers)
                values.put(AVATAR, dataAvatar)
                values.put(FAVORITE, dataFavorite)

                isFavorite = true
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
                fab_add.setImageResource(checked)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gitHelper.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.menu2 -> {
                val mIntent = Intent(this, AboutMeActivity::class.java)
                startActivity(mIntent)
            }
            R.id.menu3 -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}