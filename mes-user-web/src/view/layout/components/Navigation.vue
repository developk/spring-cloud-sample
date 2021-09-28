<template>
  <!-- color="#fafbfd" -->
  <v-navigation-drawer
    :color="config.menuColor"
    :dark="config.menuDark"
    :expand-on-hover="enableMini"
    :mini-variant="enableMini"
    :src="config.menuBgSrc"
    app
    class="menu-navigation-drawer"
    permanent
  >
    <v-list>
      <v-list-item class="px-2">
        <v-list-item-avatar>
          <v-img :src="config.menuLogoSrc"></v-img>
        </v-list-item-avatar>
        <v-list-item-title class="title text-h4">
          {{ config.menuTitle }}
        </v-list-item-title>
      </v-list-item>
    </v-list>
    <menu-group :enable-mini="enableMini" :menus="menus" />
  </v-navigation-drawer>
</template>

<script>
import {SYSTEM_CONFIG as config} from "@/config";
import MenuGroup from "./MenuGroup";

export default {
    name: "Navigation",
    components: { MenuGroup },
    props: {
      enableMini: {
        type: Boolean,
        default: () => false,
      },
      bgSrc: {
        type: String,
        default: () => require("../../../assets/logo.png"),
      },
    },
    data: () => ({
      menus: [],
      config,
    }),
    mounted() {
      this.menus = this.$store.state.menu.menus;
    },
  };
</script>

<style scoped>
  .menu-navigation-drawer {
    z-index: 9999;
  }
</style>
