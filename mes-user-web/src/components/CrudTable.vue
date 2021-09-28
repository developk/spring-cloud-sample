<template>
  <div class="height-100pc">
    <v-card class="py-2 height-100pc crud-card" elevation="1">
      <!--表格-->
      <v-data-table
        v-model="selected"
        :headers="headerArray"
        :item-key="itemKey"
        :items="records"
        :items-per-page="page.size"
        :loading="loading"
        :options.sync="pageOptions"
        :server-items-length="total"
        :show-expand="showExpand"
        :show-select="Boolean(itemKey) && checkable"
        class="crud-table elevation-0"
        fixed-header
        hide-default-footer
        @dblclick:row="rowDbclick"
      >
        <!-- 表格 头部 -->
        <template v-slot:top>
          <!--按钮及控件-->
          <v-row align="center" class="mx-2" justify="space-between" no-gutters>
            <v-col>
              <ButtonRender :buttons="buttons" @buttonClick="doAction" />
            </v-col>
            <!--控件的渲染-->
            <v-col align="end" class="pr-2">
              <template v-if="widgets && widgets.length > 0">
                <div class="col-item" @click="showFilter = !showFilter">
                  <v-icon color="#8091a5" small>mdi-filter-menu-outline</v-icon>
                  过滤
                </div>
                <FilterNavigation
                  v-model="showFilter"
                  :widget-model="queryParam"
                  :widgets="widgets"
                  @confirm="queryPage"
                />
                <span class="separator"></span>
              </template>

              <div class="col-item-readonly">
                共<span class="total-count">{{ total }}</span
                >条数据
              </div>
              <span class="separator"></span>

              <!-- 页面设置 -->
              <div class="col-item">
                <v-menu eager offset-y rounded="0">
                  <template v-slot:activator="{ on }">
                    <v-icon v-on="on" color="#8091a5" small
                      >mdi-cog-outline</v-icon
                    >
                  </template>
                  <v-list dense>
                    <v-list-item>
                      <v-list-item-content>
                        <v-menu
                          :close-on-content-click="false"
                          eager
                          left
                          nudge-left="20"
                          nudge-top="20"
                          offset-x
                          rounded="0"
                        >
                          <template v-slot:activator="{ on: fieldOn, value }">
                            <div v-on="{ ...fieldOn }" class="setting-content">
                              <span :class="[value ? 'per-current' : '']"
                                >设置显示的字段</span
                              >
                            </div>
                          </template>
                          <v-card rounded="0">
                            <div
                              id="sortable-container"
                              ref="sortable-container"
                              class="pa-2"
                            >
                              <div
                                v-for="header in sortableHeaders"
                                :key="header.text"
                                class="settable-headers "
                              >
                                <v-icon>mdi-drag-vertical</v-icon>
                                <span class="settable-text">{{
                                  header.text
                                }}</span>
                                <label>
                                  <input
                                    v-model="displayHeaderValues"
                                    :value="header.value"
                                    class="settable-checkbox"
                                    type="checkbox"
                                  />
                                </label>
                              </div>
                            </div>
                          </v-card>
                        </v-menu>
                      </v-list-item-content>
                    </v-list-item>
                    <!-- 设置每页显示的数量 -->
                    <v-list-item>
                      <v-list-item-content>
                        <v-list-item-subtitle class="setting-title">
                          每页显示数量
                        </v-list-item-subtitle>
                        <div class="setting-content">
                          <template v-for="(perPage, index) in perPageOptions">
                            <span
                              :key="index"
                              :class="[
                                page.size === perPage ? 'per-current' : '',
                              ]"
                              @click="page.size = perPage"
                              >{{ perPage }}</span
                            >
                          </template>
                        </div>
                      </v-list-item-content>
                    </v-list-item>
                  </v-list>
                </v-menu>
              </div>
            </v-col>
          </v-row>
        </template>

        <!--行内操作按钮事件-->
        <template v-slot:[`item.actions`]="{ item }">
          <v-menu
            offset-y
            origin="left bottom"
            rounded="0"
            top
            transition="scale-transition"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn v-bind="attrs" v-on="on" fab height="0" tile width="0">
                <v-icon small>mdi-dots-vertical</v-icon>
              </v-btn>
            </template>
            <v-list dense>
              <v-list-item
                v-for="(operation, index) in actions"
                :key="index"
                @click="doAction(operation.action, item, operation)"
              >
                <v-list-item-title>{{
                  operation.text || operation.action
                }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </template>

        <template v-slot:expanded-item="{ headers, item }">
          <td :colspan="headers.length">
            <slot
              :headers="headers"
              :item="item"
              name="data-expanded-item"
            ></slot>
          </td>
        </template>
      </v-data-table>

      <!--分页相关组件-->
      <v-row
        v-if="pageTotal > 1"
        class="mb-n3 mr-5 crud-table-pagination"
        justify="end"
        no-gutters
      >
        <!-- <v-col md="auto"> -->
        <v-pagination
          v-model="page.current"
          :length.sync="pageTotal"
          :total-visible.sync="pageCount"
          class="pa-2 page-selection v-size--small"
          color="primary"
          next-icon="mdi-menu-right"
          prev-icon="mdi-menu-left"
        />
        <!-- </v-col> -->
        <v-col md="auto">
          <v-text-field
            v-model="jumpToPage"
            class="mt-5"
            dense
            label="跳转到"
            style="width: 60px"
            type="number"
            @keydown="queryPage"
          />
        </v-col>
      </v-row>

      <slot name="add-dialog">
        <FormNavigation
          v-model="actionSwitch.add"
          :current-item="currentItem"
          :dialog-view="dialogView"
          @confirm="edit_"
        />
      </slot>
      <slot name="edit-dialog">
        <FormNavigation
          v-model="actionSwitch.edit"
          :current-item="currentItem"
          :dialog-view="dialogView"
          @confirm="edit_"
        />
      </slot>

      <slot name="delete-dialog">
        <tips-dialog
          v-model="actionSwitch.delete"
          title="确认要删除当前数据项吗?"
          width="300px"
          @confirm="delete_(currentItem)"
        />
      </slot>

      <slot name="delete-batch-dialog">
        <tips-dialog
          v-model="actionSwitch.batchDelete"
          title="确认要删除当前所选数据项吗?"
          width="350px"
          @confirm="batchDelete_(selected)"
        />
      </slot>

      <slot name="import-dialog">
        <v-dialog v-model="actionSwitch.import" width="400">
          <v-card class="pa-2">
            <v-row align="center" justify="space-between" no-gutters>
              <v-col align="center" class="import-col" md="6">
                <v-hover v-slot:default="{ hover }">
                  <div
                    :class="{ 'on-hover': hover }"
                    class="import-item-box"
                    @click="exportTemplate"
                  >
                    <div>
                      <v-icon color="primary" size="100"
                        >mdi-file-download-outline
                      </v-icon>
                    </div>
                    <div class="text">下载模板</div>
                  </div>
                </v-hover>
              </v-col>

              <v-col align="center" class="import-col" md="6">
                <v-hover v-slot:default="{ hover }">
                  <div :class="{ 'on-hover': hover }" class="import-item-box">
                    <div>
                      <file-upload
                        ref="import-upload"
                        v-model="importFiles"
                        :headers="authHeanders"
                        :post-action="apiBase + namespace + '/import'"
                        class="import-upload"
                        @input-file="inputFile"
                        @input-filter="inputFilter"
                      >
                        <v-icon color="primary" size="100"
                          >mdi-file-upload-outline</v-icon
                        >
                      </file-upload>
                    </div>
                    <div class="text" style="margin-top:-8px">上传数据</div>
                  </div>
                </v-hover>
              </v-col>
            </v-row>
            <v-row align="center" justify="space-between" no-gutters>
              <v-col md="12">
                <v-progress-linear
                  v-if="importFiles.length"
                  :value="importFiles[0].progress"
                  color="light-blue"
                  height="10"
                  striped
                ></v-progress-linear>
              </v-col>
            </v-row>
          </v-card>
        </v-dialog>
      </slot>
    </v-card>
  </div>
</template>

<script>
import {CrudService, mixins as crudMixins} from "@/api/crud";
import {convertWidget} from "@/utils/widget";
import TipsDialog from "@/components/TipsDialog";
import FormNavigation from "@/components/FormNavigation";
import ButtonRender from "@/components/ButtonRender";
import FilterNavigation from "@/components/FilterNavigation";
import FileUpload from "vue-upload-component";
import Sortable from "sortablejs";

crudMixins.methods.transform = convertWidget;

  export default {
    name: "CrudTable",
    mixins: [crudMixins],
    components: {
      TipsDialog,
      FormNavigation,
      ButtonRender,
      FilterNavigation,
      FileUpload,
    },
    props: {
      //表头
      headers: {
        type: Array,
        default: () => [],
      },
      /**
       * 控件
       */
      widgetModels: {
        type: Array,
        default: () => [],
      },
      /**
       * 顶部按钮
       */
      buttonModels: {
        type: Array,
        default: () => [],
      },
      showExpand: {
        type: Boolean,
        default: () => false,
      },
    },
    data: () => ({
      //表头数组
      headerArray: [],
      //每页的条数定义
      perPageOptions: [10, 20, 30],
      //页面的定义信息，外部排序，外部分页等
      pageOptions: {},
      // 页面信息，查询条数，及当前页
      page: {
        size: 10,
        current: 1,
      },
      //跳转到的页数
      jumpToPage: 1,
      //最多
      pageCount: 10,
      //总页数
      pageTotal: 1,

      //加载中...
      loading: true,
      //总记录数
      total: 0,
      //记录数组
      records: [],
      //是否可选
      checkable: true,
      //行内操作事件按钮
      loadMethod: "queryPage",
      showFilter: false,
      //可排序设置的表头，一般是第一次请求时赋值
      sortableHeaders: [],
      //显示的列头的表头value值
      displayHeaderValues: [],
    }),
    watch: {
      namespace: "initCrud",
      "page.current": function(value, old) {
        if (value === old) {
          return;
        }
        this.jumpToPage = value;
        this.queryPage();
      },
      "page.size": function(value, old) {
        if (value === old) {
          return;
        }
        this.page.current = 1;
        this.queryPage();
      },
      jumpToPage: function(value, old) {
        if (
          value === old ||
          value < 1 ||
          value === "" ||
          value > this.pageTotal
        ) {
          return;
        }
        this.page.current = Number(value);
      },
      pageOptions: {
        handler(value, old) {
          if (!Object.keys(old).length) {
            return;
          }
          this.queryPage();
        },
        deep: true,
      },
      sortableHeaders: function(newValue) {
        this.headerArray = newValue.filter(
          (header) => this.displayHeaderValues.indexOf(header.value) > -1
        );
        this.initActionHeader(this.actions);
        this.initTableExpandHeader();
      },
      displayHeaderValues: function(newValue) {
        this.headerArray = this.sortableHeaders.filter(
          (header) => newValue.indexOf(header.value) > -1
        );
        this.initActionHeader(this.actions);
        this.initTableExpandHeader();
      },
    },
    created() {
      this.initCrud();
    },
    mounted() {
      //初始化可设置表头的拖拽
      const this_ = this;
      console.warn("refef", this.$refs);
      Sortable.create(this.$refs["sortable-container"], {
        animation: 150,
        ghostClass: "sortable-ghost",
        dragClass: "sortable-drag",
        onUpdate: function(event) {
          var sortableHeaders = this_.sortableHeaders.splice(event.oldIndex, 1);
          this_.sortableHeaders.splice(event.newIndex, 0, sortableHeaders[0]);
          var newArray = this_.sortableHeaders.splice(0);
          this_.$nextTick(function() {
            this_.sortableHeaders = newArray;
          });
        },
      });
    },
    methods: {
      initCrud() {
        this.crudService = new CrudService(this.namespace);
        this.headerArray = this.headers;
        this.widgets = this.widgetModels;
        this.buttons = this.buttonModels;
        this.actions = [];
        this.pageOptions = {};
        this.queryPage();
      },
      queryPage() {
        this.loading = true;
        this.$vuetify.goTo(0, { offset: 0 });
        const queryParams = {
          ...this.page,
          ...this.queryParam,
          ...this.$route.query,
        };
        const sortBy = this.pageOptions.sortBy;
        if (sortBy && sortBy.length) {
          queryParams.sortBy = sortBy[0];
        }

        const sortDesc = this.pageOptions.sortDesc;
        if (sortDesc && sortDesc.length) {
          queryParams.sortDesc = sortDesc[0];
        }

        /**
         * 处理返回的数据
         * @param data 返回结果
         */
        const handlerRecord = function(data) {
          const records = data.records;
          const recordExtend = data.recordExtend;
          if (recordExtend) {
            records.forEach((record) => {
              const extendData = recordExtend[record[data.itemKey]];
              if (extendData) {
                Object.keys(extendData).forEach((key) => {
                  record[key] = extendData[key];
                });
              }
            });
          }
          return records;
        };

        this.crudService.list(queryParams).then((data) => {
          this.total = data.total;
          this.itemKey = data.itemKey;

          this.records = handlerRecord(data);
          this.pageTotal = data.pages;
          const pageView = data.view || {};
          this.checkable = pageView.checkable;
          if (pageView.editableView) {
            this.dialogView = pageView.editableView;
          }

          if (data.pages !== null && data.pages <= 10) {
            this.pageCount = data.pages || 1;
          }
          //若表头没定义则用数据列的
          if (!this.headerArray || this.headerArray.length === 0) {
            if (pageView.headers) {
              const displayHeaders = pageView.headers.filter(
                (item) => !item.hidden
              );
              this.headerArray = displayHeaders;

              this.sortableHeaders = JSON.parse(
                JSON.stringify(pageView.headers)
              );
              this.displayHeaderValues = displayHeaders
                .filter((item) => !item.hidden)
                .map((header) => header.value);
            } else {
              const record = data.records[0];
              Object.keys(record).forEach((key) => {
                this.headerArray.push({ text: key, value: key });
              });
            }
          }

          //初始化控件
          if (
            (!this.widgets || this.widgets.length === 0) &&
            pageView.widgets
          ) {
            this.widgets = pageView.widgets;
          }

          //初始化按钮
          if (
            (!this.buttons || this.buttons.length === 0) &&
            pageView.buttons
          ) {
            this.buttons = pageView.buttons;
          }

          //初始化列事件
          this.initActionHeader(pageView.actions);
          this.initTableExpandHeader();
          this.loading = false;
        });
      },
      rowDbclick(event, rowObject) {
        //如果有编辑的action，则双击的时候直接编辑
        if (
          this.actions &&
          this.actions.filter((ac) => "edit" === ac.action).length > 0
        ) {
          const editAction = { action: "edit", scirpt: false };
          this.doAction(editAction.action, rowObject.item, editAction);
        }
      },
      initActionHeader(actions) {
        if (
          (!this.actions || this.actions.length === 0) &&
          actions &&
          actions.length > 0
        ) {
          this.actions = actions;
        }

        if (
          this.actions &&
          this.actions.length > 0 &&
          this.headerArray.map((item) => item.value).indexOf("actions") < 0
        ) {
          this.headerArray.unshift({
            text: "",
            value: "actions",
            class: "crud-actons-td",
            align: "start",
            sortable: false,
            width: "1",
          });
        }
      },
      initTableExpandHeader() {
        if (
          this.showExpand &&
          this.headerArray
            .map((item) => item.value)
            .indexOf("data-table-expand") < 0
        ) {
          this.headerArray.push({
            text: "",
            value: "data-table-expand",
            class: "crud-actons-td",
            align: "start",
            sortable: false,
            width: "1",
          });
        }
      },
    },
  };
</script>

<style scoped>
  body >>> .v-data-table__checkbox .mdi-checkbox-marked {
    color: #3582fb;
  }

  .page-selection >>> .v-pagination__item:focus {
    border: none !important;
  }

  .crud-table >>> .v-data-table__wrapper {
    overflow: hidden;
  }

  .crud-table >>> .v-icon {
    font-size: 16px;
  }

  .crud-table >>> .mdi-checkbox-marked {
    color: #3582fb;
  }

  .crud-table >>> .v-data-table__wrapper tbody tr:nth-child(2n + 1) {
    background: #f8f8f8;
  }

  /* .crud-table >>> tbody tr:hover {
    box-shadow: 0 3px 15px -2px rgba(0, 0, 0, 0.12);
    transform: translateY(-2px);
    background: #fff !important;
  } */

  .crud-table >>> .crud-actons-td {
    padding: 0 1px !important;
  }

  .crud-table >>> .actions-menu {
    border-radius: unset !important;
  }

  .col-item,
  .col-item-readonly {
    display: inline-block;
    font-size: 14px;
    color: #8091a5;
    cursor: pointer;
  }

  .col-item:hover,
  .col-item:hover > * {
    color: #3582fb !important;
  }

  .total-count {
    color: #000;
    padding: 0 5px;
  }

  .per-page {
    display: inline-block;
    width: 30px;
    height: 20px;
  }

  .separator {
    border-right: 1px solid #dfe6ee;
    width: 1px;
    height: 18px;
    margin-left: 15px;
    margin-right: 15px;
  }

  .setting-title {
    min-width: 20px;
    font-size: 12px;
    color: #8091a5;
  }

  .setting-content {
    margin-top: 5px;
  }

  .setting-content span {
    font-size: 12px;
    padding: 3px;
    cursor: pointer;
    margin: 0 1px;
    border: 1px solid #ebebeb;
  }

  .setting-content span:hover {
    background: #d7e6fe;
    color: #3582fb;
    border: 1px solid #3582fb !important;
    z-index: 1;
    cursor: pointer;
  }

  .per-current {
    background: #d7e6fe;
    color: #3582fb;
    border: 1px solid #3582fb !important;
    z-index: 1;
    cursor: default;
  }

  .crud-table >>> .v-progress-linear {
    height: 1px !important;
  }

  .crud-table >>> .v-data-table__expanded__content {
    box-shadow: unset;
  }

  .import-item-box {
    margin: 10px;
    cursor: pointer;
    background-color: #fafafa;
    transition: opacity 0.4s ease-in-out;
  }

  .import-item-box:not(.on-hover) {
    opacity: 0.6;
  }

  .import-upload >>> label {
    cursor: pointer;
  }

  .import-item-box .text {
    color: #80abfa;
    font-weight: bold;
    padding-bottom: 10px;
  }

  .settable-headers {
    position: relative;
    padding: 8px 5px 8px 5px;
    margin: auto auto 2px;
    border-radius: 4px;
    font-size: 14px;
  }

  .settable-text {
    padding-right: 20px;
  }

  .settable-checkbox {
    position: absolute;
    right: 5px;
    top: 12px;
  }

  .settable-headers:hover {
    cursor: move;
  }

  .sortable-ghost {
    background-color: #ffffff;
  }

  .sortable-drag {
    background-color: #ffffff;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.13);
  }
</style>
