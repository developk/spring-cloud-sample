<template>
  <v-menu
    v-model="showSwitch"
    :close-on-content-click="false"
    :nudge-right="40"
    min-width="290px"
    offset-y
    transition="scale-transition"
  >
    <template v-slot:activator="{ on }">
      <v-text-field
        v-bind="$attrs"
        v-on="on"
        :label="label"
        :value="dateText"
        clearable
        readonly
        @click:clear="clearDate"
      ></v-text-field>
    </template>
    <v-date-picker
      v-model="date"
      :first-day-of-week="0"
      locale="zh-cn"
      show-current
      @input="cofirmDate"
    ></v-date-picker>
  </v-menu>
</template>

<script>
import moment from "moment"; //导入文件
moment.locale("zh-cn"); //需要汉化
  export default {
    name: "DataPicker",
    props: {
      format: {
        type: String,
        default: () => "YYYY-MM-DD",
      },
      label: {
        type: String,
        default: () => "",
      },
      value: {
        type: String,
        default: () => "",
      },
    },
    data: () => ({
      showSwitch: false,
      date: "",
    }),
    watch: {
      value() {
        this.date = this.value ? this.value : "";
      },
    },
    computed: {
      dateText: {
        get() {
          return this.date ? String(moment(this.date).format(this.format)) : "";
        },
      },
    },
    methods: {
      cofirmDate() {
        this.$emit("input", this.dateText);
        this.showSwitch = false;
      },
      clearDate() {
        this.date = null;
        this.cofirmDate();
      },
    },
  };
</script>

<style></style>
