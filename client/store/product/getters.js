const getters = {
  allProducts: (state) => state.allProducts,
  productList: (state) => state.allProducts.slice(0, 4),
  recentProducts: (state) => state.allProducts.slice(0, 8),
  relatedProducts: (state) => state.allProducts.slice(0, 3),
}

export default getters
