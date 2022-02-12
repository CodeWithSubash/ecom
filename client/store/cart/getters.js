const getters = {
  cartItemCount: (state) => state.cartItems.length,
  cartSubtotal: (state) =>
    state.cartItems.reduce((acc, cartItem) => {
      return (acc += cartItem?.product.price * cartItem.quantity)
    }, 0),
}

export default getters
