package com.it1shka.checkers.screens.profile

import kotlin.random.Random

private val firstParts = listOf(
  "Abundant", "Adorable", "Adventurous", "Agile", "Alert",
  "Amazing", "Ambitious", "Amiable", "Ample", "Ancient",
  "Animated", "Appetizing", "Arid", "Artful", "Artistic",
  "Astonishing", "Athletic", "Attentive", "Audacious", "Authentic",
  "Beautiful", "Benevolent", "Blissful", "Bold", "Brave",
  "Bright", "Brilliant", "Broad", "Bubbly", "Buoyant",
  "Calm", "Carefree", "Careful", "Caring", "Charming",
  "Cheerful", "Clever", "Colorful", "Compassionate", "Confident",
  "Courageous", "Creative", "Crisp", "Dashing", "Dazzling",
  "Delightful", "Determined", "Diligent", "Distinct", "Dynamic",
  "Eager", "Efficient", "Elegant", "Eloquent", "Energetic",
  "Enthusiastic", "Eternal", "Excellent", "Exciting", "Exuberant",
  "Fabulous", "Faithful", "Fancy", "Fantastic", "Fearless",
  "Flawless", "Flexible", "Flourishing", "Fortunate", "Friendly",
  "Frosty", "Gentle", "Genuine", "Giant", "Gifted",
  "Glorious", "Golden", "Graceful", "Gracious", "Grand",
  "Grateful", "Great", "Handsome", "Harmonious", "Healthy",
  "Heroic", "Honest", "Hopeful", "Humble", "Humorous"
)

private val secondParts = listOf(
  "Anchor", "Apple", "Arrow", "Ball", "Banner",
  "Basket", "Beacon", "Bell", "Blade", "Book",
  "Bottle", "Bridge", "Bucket", "Candle", "Cannon",
  "Card", "Cart", "Castle", "Chain", "Chair",
  "Chest", "Clock", "Cloud", "Compass", "Crystal",
  "Cup", "Diamond", "Door", "Drum", "Feather",
  "Flag", "Flame", "Flower", "Fountain", "Gem",
  "Globe", "Hammer", "Hat", "Heart", "Helmet",
  "Horn", "House", "Key", "Lantern", "Leaf",
  "Letter", "Lock", "Map", "Medal", "Mirror",
  "Moon", "Mountain", "Orb", "Palette", "Path",
  "Pen", "Piano", "Picture", "Pillar", "Ring",
  "Rocket", "Rope", "Shield", "Ship", "Shoe",
  "Skull", "Spear", "Sphere", "Star", "Statue",
  "Stone", "Sword", "Table", "Tent", "Throne",
  "Torch", "Tower", "Tree", "Trophy", "Trumpet",
  "Vase", "Wagon", "Wheel", "Window", "Wing"
)

fun generateNickname(): String {
  val firstPart = firstParts.random()
  val secondPart = secondParts.random()
  val postfix = Random.nextInt(100, 1000)
  return firstPart + secondPart + postfix.toString()
}