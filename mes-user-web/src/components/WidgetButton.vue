<template>
  <!--按钮及控件-->
  <v-row class="mt-2" no-gutters>
    <!--按钮的渲染-->
    <v-col class="button-col pr-10 flex-column" md="auto">
      <template v-for="(button, index) in buttons">
        <v-btn
            v-if="!button.group"
            :key="index"
            :color="button.color"
            class="my-1 ml-2"
            depressed
            small
            tile
            @click="buttonClick(button.action)"
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
                @click="buttonClick(operation.action)"
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
    </v-col>

    <!--控件的渲染-->
    <v-col v-for="(widget, index) in widgets" :key="index" class="pl-10" md="2">
      <component
          :is="widget.widgetName"
          v-model="modelObject[widget.key]"
          v-bind="convert(widget)"
          @change="widgetChange"
      />
    </v-col>
  </v-row>
</template>

<script>
import {convertWidget} from "@/utils/widget";

export default {
  name: "WidgetButton",
  props: {
    widgets: Array,
    buttons: Array,
    modelObject: Object,
  },
  methods: {
    convert(widget) {
      return convertWidget(widget);
    },
    widgetChange() {
      this.$emit("widgetChange");
    },
    buttonClick(action) {
      this.$emit("buttonClick", action);
    },
  },
};
</script>
