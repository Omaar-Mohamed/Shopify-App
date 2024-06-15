package com.example.shopify_app.features.Review.data

import com.example.shopify_app.R

data class Review(
    val name: String,
    val date: String,
    val rate: Int,
    val review: String,
    val image: Int
) {
    companion object {
        fun reviewsList(): List<Review> {
            return listOf(
                Review(name = "Mohamed Ahmed", date = "2024-01-01", rate = 5, review = "Outstanding service!", R.drawable.img1),
                Review(name = "Ali Ahmed", date = "2024-02-02", rate = 4, review = "Very good, but room for improvement.",R.drawable.img2),
                Review(name = "Omar Mohamed", date = "2024-03-03", rate = 3, review = "Average experience.",R.drawable.img3),
                Review(name = "Ahmed Khaled", date = "2024-04-04", rate = 2, review = "Not up to the mark.",R.drawable.img4),
                Review(name = "Mohamed Saad", date = "2024-05-05", rate = 1, review = "Very disappointing.",R.drawable.img5),
                Review(name = "Ahmed Refat", date = "2024-06-06", rate = 5, review = "Simply perfect!",R.drawable.img6),
                Review(name = "Mina Emad", date = "2024-07-07", rate = 4, review = "Great, but a bit pricey.",R.drawable.img7),
                Review(name = "Nermen Mahmoud", date = "2024-08-08", rate = 3, review = "It was okay.",R.drawable.img8),
                Review(name = "Heba Mohamed", date = "2024-09-09", rate = 2, review = "Could be better.",R.drawable.img9),
                Review(name = "Mai Taha", date = "2024-10-10", rate = 1, review = "Terrible experience.",R.drawable.img10)
            )
        }
    }
}