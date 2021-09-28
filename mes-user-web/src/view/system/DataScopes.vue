// 数据范围管理列表
<template>
  <div class="height-100pc">
    <crud-table
      ref="$crud$"
      :exclude-actions="['edit']"
      namespace="/rest/scopes"
      @action="actionEvent"
    >
      <template v-slot:add-dialog>
        <simple-form-navigation
          v-model="addDialog"
          :successAction="saveEntity"
          width="800"
          @callback="reload"
        >
          <v-card elevation="0">
            <v-card-subtitle>基础信息</v-card-subtitle>
            <v-card-text>
              <v-text-field
                v-model="scopeEntity.scopeName"
                :rules="[
                  (v) =>
                    (v !== undefined && v !== null && v !== '') ||
                    `数据范围名称不能为空`,
                ]"
                clearable
                label="数据范围名称"
              />
              <v-text-field
                v-model="scopeEntity.describe"
                clearable
                label="描述"
              />
            </v-card-text>

            <v-divider class="mx-4" />

            <v-card-subtitle color="primary">数据范围设置</v-card-subtitle>
            <v-card-text>
              <v-row class="pa-4">
                <!--组织机构选择-->
                <v-col class="select-container" cols="6">
                  <tree-model
                    :display-root="true"
                    :return-object="false"
                    :searchable="true"
                    :selectable="true"
                    :value="scopeEntity.selectedOrganIds"
                    namespace="/rest/organs"
                    select-type="independent"
                    @nodeActive="treeNodeActive"
                    @selection="selection"
                  />
                </v-col>

                <v-divider vertical />
                <v-col class="mt-0 ml-3 align-center" cols="5">
                  <template v-if="scopeRuleShow">
                    <div>范围规则</div>
                    <v-divider />
                    <v-radio-group
                      v-model="
                        scopeEntity.scopeDefinesMap[selected]['scopeRule']
                      "
                      :mandatory="true"
                      :row="true"
                      class="ml-1"
                    >
                      <v-radio label="包含" value="0" />
                      <v-radio label="排除" value="1" />
                    </v-radio-group>
                  </template>

                  <template v-if="scopeTypeShow">
                    <div>范围类型</div>
                    <v-divider />
                    <!--数据范围类型的选择-->
                    <v-checkbox
                      v-for="(scopeType, index) in scopeTypes"
                      :key="index"
                      v-model="scopeEntity.scopeDefinesMap[selected]['types']"
                      :label="scopeType.text"
                      :value="scopeType.code"
                      class="ml-3 mb-n4"
                      dense
                    />
                  </template>
                </v-col>
              </v-row>
            </v-card-text>
          </v-card>
        </simple-form-navigation>
      </template>
    </crud-table>
  </div>
</template>

<script>
import CrudTable from "../../components/CrudTable";
import TreeModel from "../../components/TreeModel";
import {getScopeTypes} from "@/api/auth";
import SimpleFormNavigation from "@/components/SimpleFormNavigation";

const DEFAULT_ENTITY = {
    scopeId: null,
    scopeName: "",
    describe: "",
    selectedOrganIds: [],
    scopeDefinesMap: {},
  };

  export default {
    name: "DataScopes",
    components: { CrudTable, TreeModel, SimpleFormNavigation },
    data: () => ({
      addDialog: false,
      scopeEntity: JSON.parse(JSON.stringify(DEFAULT_ENTITY)),
      open: [],
      scopeTypes: [],
      selected: {},
      //观察数据是否已经准备就续,主要是用于动态数组对象的更新控制
      hasReadiness: false,
    }),
    computed: {
      scopeRuleShow: function() {
        return (
          this.selected &&
          this.scopeEntity.scopeDefinesMap[this.selected] &&
          this.hasReadiness
        );
      },
      scopeTypeShow: function() {
        return (
          this.scopeTypes &&
          this.scopeTypes.length > 0 &&
          this.selected &&
          this.scopeEntity.scopeDefinesMap[this.selected] &&
          this.hasReadiness
        );
      },
    },
    watch: {
      selected: {
        handler(nodeId) {
          if (nodeId) {
            this.initNodeScopeDefine(nodeId);
          }
        },
      },
    },
    methods: {
      //初始化必要的数据
      initNecessary() {
        getScopeTypes().then((data) => {
          this.scopeTypes = data;
        });
      },
      //执行的事件
      actionEvent(action, item) {
        switch (action) {
          case "add": {
            this.scopeEntity = JSON.parse(JSON.stringify(DEFAULT_ENTITY));
            this.addDialog = true;
            break;
          }
          case "edit": {
            const scopeDefinesMap = {},
              selectedOrganIds = [];
            if (item.scopeDefines && item.scopeDefines.length > 0) {
              item.scopeDefines.forEach((scopeDefine) => {
                selectedOrganIds.push(scopeDefine.organId);
                scopeDefinesMap[scopeDefine.organId] = {
                  scopeRule: String(scopeDefine.scopeRule),
                  types: scopeDefine.scopeTypes || [],
                };
              });
            }
            this.scopeEntity = {
              ...item,
              scopeDefinesMap: scopeDefinesMap,
              selectedOrganIds: selectedOrganIds,
            };
            this.$nextTick(() => {
              this.addDialog = true;
            });

            break;
          }
          default:
        }
      },
      treeNodeActive(nodeIds) {
        this.selected = nodeIds && nodeIds.length > 0 ? nodeIds[0] : null;
      },
      selection(nodes) {
        if (nodes) {
          this.scopeEntity.selectedOrganIds = nodes;
        }
      },
      //初始化节点的数据范围的定义规则
      initNodeScopeDefine(nodeId) {
        if (!this.scopeEntity.scopeDefinesMap[nodeId]) {
          this.hasReadiness = false;
          this.scopeEntity.scopeDefinesMap[nodeId] = {
            scopeRule: "0",
            types: [],
          };
          this.$nextTick(() => {
            this.hasReadiness = true;
          });
        }
      },
      saveEntity() {
        const submitEntity = { ...this.scopeEntity };
        submitEntity.scopeDefines = Object.keys(
          submitEntity.scopeDefinesMap
        ).map((scopeDefineId) => {
          const scopeDefinesMapElement =
            submitEntity.scopeDefinesMap[scopeDefineId];
          return {
            scopeId: submitEntity.scopeId,
            organId: scopeDefineId,
            scopeRule: scopeDefinesMapElement.scopeRule,
            scopeTypes: scopeDefinesMapElement.types || [],
          };
        });
        return new Promise((resolve) => {
          this.$refs["$crud$"].crudService
            .save(submitEntity)
            .then(() => {
              this.$toast.success("操作成功", {
                position: "top-center",
              });
              resolve();
            })
            .catch((err) => {
              this.$toast.error(err.message, {
                position: "top-center",
              });
            });
        });
      },
      reload() {
        this.$refs["$crud$"].reload();
      },
    },
    created() {
      this.initNecessary();
    },
  };
</script>

<style scoped>
  .select-container {
    height: 320px !important;
    overflow: auto !important;
    width: 100% !important;
  }
</style>
