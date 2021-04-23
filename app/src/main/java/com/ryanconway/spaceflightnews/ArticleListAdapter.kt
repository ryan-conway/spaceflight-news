package com.ryanconway.spaceflightnews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ryanconway.spaceflightnews.databinding.CardArticleBinding
import com.ryanconway.spaceflightnews.databinding.ItemLoadMoreBinding
import com.ryanconway.spaceflightnews.domain.model.Article

class ArticleListAdapter(
    private val articleCallback: (Article) -> Unit,
    private val loadMoreCallback: () -> Unit
) : ListAdapter<Article, RecyclerView.ViewHolder>(ArticleDiffCallback()) {

    private var isLoading: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder{
        return if (viewType == ArticleListViewType.ARTICLE.ordinal) {
            ArticleViewHolder.from(parent)
        } else {
            LoadMoreViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticleViewHolder) {
            holder.bind(getItem(position), articleCallback)
        } else if (holder is LoadMoreViewHolder) {
            holder.bind(isLoading, loadMoreCallback)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    fun getArticleCount(): Int = itemCount - 1

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyItemChanged(itemCount - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < getArticleCount()) {
            ArticleListViewType.ARTICLE.ordinal
        } else {
            ArticleListViewType.LOAD_MORE.ordinal
        }
    }
}

enum class ArticleListViewType {
    ARTICLE,
    LOAD_MORE
}

class ArticleViewHolder(
    private val binding: CardArticleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article?, callback: (Article) -> Unit) {
        article ?: return
        binding.title.text = article.title
        binding.summary.text = article.summary

        Glide.with(binding.image)
            .load(article.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_twotone_image_24)
            .error(R.drawable.ic_twotone_broken_image_24)
            .into(binding.image)

        binding.root.setOnClickListener { callback(article) }
    }

    companion object {

        fun from(parent: ViewGroup) = ArticleViewHolder(
            CardArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class LoadMoreViewHolder(
    private val binding: ItemLoadMoreBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(isLoading: Boolean, callback: () -> Unit) {
        if (isLoading) {
            setLoading()
        } else {
            setLoadMore(callback)
        }
    }

    private fun setLoadMore(callback: () -> Unit) {
        binding.text.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.root.setOnClickListener {
            setLoading()
            callback()
        }
    }

    private fun setLoading() {
        binding.text.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.root.setOnClickListener(null)
    }

    companion object {

        fun from(parent: ViewGroup) = LoadMoreViewHolder(
            ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem == newItem
}