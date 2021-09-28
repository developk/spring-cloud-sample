<template>
  <!--  -->
  <v-navigation-drawer
    v-model="dialogSwitch"
    :height="navigationHeight"
    :style="{ top: `${scrollTop}px` }"
    :temporary="temporary"
    :width="dialogView.width || width"
    absolute
    class="nav-form elevation-0"
  >
    <v-form ref="form">
      <!-- 字段渲染 -->
      <v-container>
        <v-row>
          <template v-for="(editFieldWidget, index) in editFieldWidgets">
            <v-col
              :key="index"
              :md="getColMd(editFieldWidget, editFieldWidgets.length)"
            >
              <component
                :is="editFieldWidget.widgetName"
                v-model="currentItem[editFieldWidget.key]"
                v-bind="editFieldWidget"
              />
            </v-col>
          </template>
        </v-row>

        <v-divider />
        <v-row justify="end">
          <v-col align="end" md="4">
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
            <v-btn class="mr-2" color="primary" small tile @click="save">
              {{ saveText }}
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </v-navigation-drawer>
</template>

<script>
import {convertFieldWidget} from "@/utils/widget";

const mixins = {
    methods: {
      convertFieldWidget,
    },
  };
  export default {
    name: "FormNavigation",
    mixins: [mixins],
    props: {
      value: Boolean,
      temporary: {
        type: Boolean,
        default: () => true,
      },
      width: {
        type: String || Number,
        default: () => "500",
      },
      libNameSpace: {
        type: String,
        default: () => "../template",
      },
      dialogView: Object,
      currentItem: Object,
      cancelText: {
        type: String,
        default: () => "关闭",
      },
      saveText: {
        type: String,
        default: () => "保存",
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
          this.$refs.form.resetValidation();
          //grants_改变由父组件控制
          this.$emit("input", val);
        },
      },
      editFields: {
        get() {
          return this.dialogView.editFields;
        },
      },
      editFieldWidgets: {
        get() {
          if (this.editFields) {
            return this.editFields.map((editField) =>
              this.convertFieldWidget(editField)
            );
          }
          return null;
        },
      },
    },
    watch: {
      dialogSwitch: function(data) {
        if (data) {
          this.handleScroll();
        }
      },
    },
    mounted() {
      // window.addEventListener("scroll", this.handleScroll, true);
    },
    methods: {
      save() {
        if (this.$refs.form.validate()) {
          this.$emit("confirm", this.currentItem);
        }
      },
      getColMd(widget, widgetsLength) {
        const col12Widgets = ["v-textarea"];
        if (col12Widgets.indexOf(widget.widgetName) > -1) {
          return 12;
        }

        return 12 / (widgetsLength > 2 ? 2 : widgetsLength);
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

<style>
  .nav-form {
    z-index: 99;
  }
</style>
