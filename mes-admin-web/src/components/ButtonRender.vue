<template>
  <div>
    <template v-for="(button, index) in buttons">
      <v-btn
        v-if="!button.group"
        :key="index"
        :color="button.color"
        class="my-1 ml-2"
        depressed
        small
        tile
        @click="buttonClick(button)"
      >
        <v-icon v-if="button.icon" left small>mdi-{{ button.icon }}</v-icon>
        {{ button.text }}
      </v-btn>
      <v-menu
        v-else
        :key="index"
        bottom
        offset-y
        rounded="0"
        transition="slide-y-transition"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            v-bind="attrs"
            v-on="on"
            :color="button.color"
            class="my-1 ml-2"
            depressed
            outlined
            small
            tile
          >
            {{ button.text }}
            <v-icon right small>mdi-{{ button.icon }}</v-icon>
          </v-btn>
        </template>
        <v-list dense>
          <v-list-item
            v-for="(operation, index) in button.actions"
            :key="index"
            :color="operation.color"
            @click="buttonClick(operation)"
          >
            <v-list-item-icon>
              <v-icon :color="operation.color" left small
                >mdi-{{ operation.icon }}
              </v-icon>
            </v-list-item-icon>
            <v-list-item-title>
              {{ operation.text || operation.action }}
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </template>
  </div>
</template>

<script>
  export default {
    name: "ButtonGroup",
    props: {
      buttons: Array,
    },
    methods: {
      buttonClick(button) {
        this.$emit("buttonClick", button.action,null,button);
      },
    },
  };
</script>

<style></style>
