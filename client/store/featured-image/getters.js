const mapFeaturedImage = (fi) => {
  return {
    categoryId: fi.categoryId,
    description: fi.description,
    image: fi.files.fileDownloadUri,
    title: fi.title,
    subtitle: fi.subTitle,
  }
}

const getters = {
  bannerSlides: (state) =>
    state.featuredImages
      .filter((fi) => !fi.deletedAt && fi.featuredType === 'BANNER_SLIDER')
      .map((bs) => mapFeaturedImage(bs)),

  fixedBanners: (state) =>
    state.featuredImages
      .filter((fi) => !fi.deletedAt && fi.featuredType === 'BANNER_FIXED')
      .map((fb) => mapFeaturedImage(fb)),

  featuredCollections: (state) =>
    state.featuredImages
      .filter((fi) => !fi.deletedAt && fi.featuredType === 'FEATURED_TOP')
      .map((fc) => mapFeaturedImage(fc)),

  featuredAd: (state) =>
    state.featuredImages
      .filter((fi) => !fi.deletedAt && fi.featuredType === 'FEATURED_AD')
      .map((fc) => mapFeaturedImage(fc)),
}

export default getters
