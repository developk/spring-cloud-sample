<template>
  <v-navigation-drawer
    v-model="dialogSwitch"
    :height="navigationHeight"
    :style="{ top: `${scrollTop}px` }"
    :width="width"
    absolute
    hide-overlay
    right
    temporary
  >
    <!-- 字段渲染 -->
    <v-container>
      <v-row v-if="widgetModel" class="px-4">
        <v-col v-for="(widget, index) in widgets" :key="index" :md="12">
          <widget-render :model-object="widgetModel" :widget="widget" />
        </v-col>
      </v-row>

      <v-divider></v-divider>
      <v-row align="end" justify="end">
        <v-col>
          <v-btn
            class="mr-2"
            color="secondary"
            outlined
            small
            tile
            @click="dialogSwitch = !dialogSwitch"
          >
            {{ cancelText }}
          </v-btn>
          <v-btn class="mr-2" color="primary" small tile @click="confirm">
            {{ confirmText }}
          </v-btn>
        </v-col>
      </v-row>
    </v-container>

    <!-- 按钮 -->
    <v-container> </v-container>
  </v-navigation-drawer>
</template>

<script>
import WidgetRender from "@/components/WidgetRender";

export default {
    name: "FilterNavigation",
    components: { WidgetRender },
    props: {
      value: Boolean,
      width: {
        type: String || Number,
        default: () => "400",
      },
      widgets: Array,
      widgetModel: Object,
      cancelText: {
        type: String,
        default: () => "取消",
      },
      confirmText: {
        type: String,
        default: () => "过滤",
      },
    },
    data: () => ({
      scrollTop: 0,
      navigationHeight: "100%",
    }),
    computed: {
      dialogSwitch: {
        get() {
          return this.value;
        },
        set(val) {
          //grants_改变由父组件控制
          this.$emit("input", val);
        },
      },
    },
    watch: {
      modelObject: function() {
        console.warn(this.modelObject);
      },
      dialogSwitch: function(data) {
        if (data) {
          this.handleScroll();
        }
      },
    },
    methods: {
      confirm() {
        this.$emit("confirm");
      },
      handleScroll() {
        const scrollTop =
          window.pageYOffset ||
          document.documentElement.scrollTop ||
          document.body.scrollTop;
        if (scrollTop && scrollTop !== 0) {
          this.scrollTop = scrollTop - 12;
        } else {
          this.scrollTop = 0;
        }

        this.navigationHeight =
          document.body.scrollHeight - this.scrollTop - 108;
      },
    },
  };
</script>

<style></style>
