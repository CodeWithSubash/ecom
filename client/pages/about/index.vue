<template>
  <div class="default-container mt-24 py-16">
    <div
      v-for="(content, index) in sampleContents"
      :key="content.title"
      class="pb-24 last:pb-0"
      data-aos="fade-up"
      data-aos-anchor-placement="top-center"
    >
      <section-header>
        <template #title>{{ content.title }}</template>
        <template #subtitle>{{ content.subtitle }}</template>
      </section-header>
      <div class="text-gray-700 font-thin">
        <img
          :src="content.image"
          alt="About Us placeholder image"
          class="h-96 w-2/3 mb-4 object-cover object-center"
          :class="[index % 2 ? 'float-right ml-4' : 'float-left  mr-4']"
        />
        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sunt laborum
        natus, magnam possimus ducimus labore nisi dolores et vitae aperiam
        consequatur totam obcaecati voluptatibus soluta ad reiciendis fugit
        error odio laboriosam quia! Odit velit adipisci consequatur ipsa, quam
        fuga quisquam animi iusto iste modi vero doloribus corrupti totam
        aspernatur dolorem dolor deserunt libero, eveniet quo excepturi.
        Ducimus, delectus nobis blanditiis saepe accusamus deleniti quis quae,
        corporis distinctio beatae incidunt tenetur nulla esse. Rerum, expedita
        culpa maiores hic tempora debitis vitae architecto voluptatibus
        reiciendis officiis unde ex impedit mollitia, ullam, vero corrupti.
        Optio et quidem placeat saepe molestiae voluptatem labore laboriosam
        dolorem voluptas amet blanditiis assumenda eius quisquam omnis, libero
        nam corporis quos officia iusto inventore. Debitis odit blanditiis totam
        dignissimos, numquam ipsa rerum non voluptates quae distinctio iste
        voluptas itaque corrupti commodi. Dolorum vel incidunt voluptas
        assumenda repellendus laudantium, voluptatibus itaque, modi quam fugit
        nesciunt est iusto architecto qui, asperiores dignissimos recusandae
        aperiam doloremque! Architecto alias quibusdam dolores autem eligendi
        impedit consequuntur minima corporis, inventore numquam neque voluptas
        nihil perferendis, dignissimos iure doloremque provident non repellat
        expedita voluptates delectus sequi. Laborum aliquam, asperiores
        cupiditate esse qui voluptas ea officia, excepturi, repellat nam maxime
        maiores quae culpa voluptates debitis obcaecati sunt similique provident
        deleniti? Minus magnam nobis alias ab, accusamus similique eaque
        assumenda eligendi a labore, blanditiis illum, sint saepe incidunt
        numquam atque aut sunt ad ratione ipsum qui pariatur possimus!
        Consequatur, autem? Laudantium, ea sed consectetur velit repudiandae
        dolores nemo, veritatis in expedita a ipsum autem recusandae iste
        officia possimus doloremque tenetur quas vel. Minima harum officia
        delectus fugit architecto nulla eos at beatae labore aut? Saepe, iste
        eum. Ab optio dolores dolor impedit nihil quidem aperiam quas, quisquam
        deserunt nobis tempora eos reprehenderit enim, illum, architecto vel at
        voluptatem expedita assumenda qui deleniti amet iste. Voluptatem porro
        non voluptatibus.
      </div>
    </div>
  </div>
</template>

<script>
import SectionHeader from '~/components/home/SectionHeader.vue'
export default {
  name: 'About',

  head: {
    title: 'KMPL | About Us',
  },

  components: { SectionHeader },

  layout: 'guest',

  data() {
    return {
      sampleContents: [
        {
          title: 'About Us',
          subtitle: 'Get to know about us',
          image: require('~/assets/placeholders/manufacturer.jpg'),
        },
        {
          title: 'Our Vision',
          subtitle: 'What we have in our sight for the futue',
          image: require('~/assets/placeholders/company-vision.jpg'),
        },
        {
          title: 'Mission',
          subtitle: 'Things we have to achieve',
          image: require('~/assets/placeholders/company-mission.jpg'),
        },
      ],
    }
  },
}
</script>

<style></style>
