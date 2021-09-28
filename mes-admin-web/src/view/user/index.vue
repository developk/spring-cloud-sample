<template>
  <div class="py-2 px-2 height-100pc user-center" elevation="1">
    <v-row justify="space-between" no-gutters>
      <v-col class="mr-3" md="3">
        <v-card shaped>
          <v-img
              :style="{ 'background-image': usercard }"
              class="user-card-img"
              height="250"
          >
            <div class="text-center mt-2">
              <img
                  v-if="profileForm.avatar"
                  :src="profileForm.avatar"
                  alt=""
                  class="avatar-img"/>
              <v-icon v-else color="#fff" size="190">mdi-account-circle</v-icon>
            </div>
            <div class="my-2 text-center font-weight-bold">
              {{ userInfo.username }}
            </div>
          </v-img>

          <v-card-text>
            <div v-for="(line, index) in cardlines" :key="index">
              <div class="py-2">
                <v-icon left small>{{ line.icon }}
                </v-icon
                >
                {{ line.text }}
              </div>
              <v-divider/>
            </div>
            <div class="mb-3"></div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col class="ml-3">
        <v-card>
          <v-tabs>
            <v-tab>我的动态</v-tab>
            <v-tab>个人设置</v-tab>
            <v-tab>修改密码</v-tab>

            <!-- 我的动态 -->
            <v-tab-item>
              <v-card class="pa-3" flat>
                <v-timeline>
                  <v-timeline-item
                      v-for="(log, index) in accessLogs"
                      :key="index"
                      small
                  >
                    <template v-slot:icon></template>
                    <span slot="opposite">{{
                        $moment(log.createTime).format("YYYY-MM-DD hh:mm:ss")
                      }}</span>
                    <v-card class="elevation-2">
                      <v-card-title class="headline">{{
                          log.resourceName || log.url
                        }}
                      </v-card-title>
                    </v-card>
                  </v-timeline-item>
                </v-timeline>
              </v-card>
            </v-tab-item>

            <!-- 个人设置 -->
            <v-tab-item>
              <v-card class="pa-3" flat>
                <v-row>
                  <v-col md="6">
                    <v-form ref="form">
                      <!-- 字段渲染 -->
                      <v-container>
                        <v-row>
                          <v-col md="12">
                            <v-text-field
                                v-model="profileForm.username"
                                :rules="profileRules.username"
                                clearable
                                label="用户名"
                            />
                          </v-col>
                        </v-row>
                        <v-row>
                          <v-col md="12">
                            <v-text-field
                                v-model="profileForm.mobile"
                                :rules="profileRules.mobile"
                                clearable
                                label="手机号码"
                            />
                          </v-col>
                        </v-row>
                        <v-row>
                          <v-col md="12">
                            <v-text-field
                                v-model="profileForm.email"
                                :rules="profileRules.email"
                                clearable
                                label="邮箱"
                            />
                          </v-col>
                        </v-row>
                      </v-container>
                    </v-form>
                  </v-col>
                  <v-col md="6">
                    <avatar-upload
                        v-model="profileForm.avatar"
                        tips-text="拖拽或单击进行上传图片"
                    />
                  </v-col>
                </v-row>
                <v-divider/>
                <v-row justify="end">
                  <v-col align="end" md="4">
                    <v-btn
                        class="mr-2"
                        color="secondary"
                        outlined
                        small
                        tile
                        @click="resetProfile"
                    >
                      重置
                    </v-btn>
                    <v-btn
                        class="mr-2"
                        color="primary"
                        small
                        tile
                        @click="saveProfile"
                    >
                      保存
                    </v-btn>
                  </v-col>
                </v-row>
              </v-card>
            </v-tab-item>

            <!-- 修改密码 -->
            <v-tab-item>
              <v-card class="pa-3" flat>
                <v-row align="center" justify="center">
                  <v-col md="6">
                    <v-form ref="passwordForm">
                      <!-- 字段渲染 -->
                      <v-container>
                        <v-row>
                          <v-col md="12">
                            <v-text-field
                                v-model="passwordForm.oldPassword"
                                :rules="passwordRules.oldPassword"
                                clearable
                                label="旧密码"
                                type="password"
                            />
                          </v-col>
                        </v-row>
                        <v-row>
                          <v-col md="12">
                            <v-text-field
                                id="newPassword"
                                v-model="passwordForm.newPassword"
                                :rules="passwordRules.newPassword"
                                clearable
                                label="新密码"
                                type="password"
                            />
                          </v-col>
                        </v-row>
                        <v-row>
                          <v-col md="12">
                            <v-text-field
                                id="confirmPassword"
                                v-model="passwordForm.confirmPassword"
                                :rules="passwordRules.confirmPassword"
                                clearable
                                label="确认密码"
                                type="password"
                            />
                          </v-col>
                        </v-row>
                      </v-container>
                    </v-form>
                  </v-col>
                </v-row>
                <v-row justify="end">
                  <v-col align="end" md="4">
                    <v-btn
                        class="mr-2"
                        color="primary"
                        small
                        tile
                        @click="passwordUpate"
                    >
                      保存
                    </v-btn>
                  </v-col>
                </v-row>
              </v-card>
            </v-tab-item>
          </v-tabs>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import GeoPattern from "geopattern";
import {getAccessLogsByUserId, saveUserProfile, updatePassword,} from "@/api/user";
import AvatarUpload from "@/components/AvatarUpload";
import {required} from "@/utils/widget";

export default {
  name: "UserCenter",
  components: {
    AvatarUpload,
  },
  data() {
    return {
      alert: false,
      usercard: "",
      userInfo: {},
      cardlines: [],
      tabItems: [
        {tab: "logs", text: "我的动态"},
        {tab: "profile", content: "个人设置"},
        {tab: "password", content: "修改密码"},
      ],
      accessLogs: [],
      profileForm: {
        userId: "",
        username: "",
        mobile: "",
        email: "",
        avatar: "",
      },
      profileRules: {
        username: [required("用户名")],
        mobile: [
          required("手机号码"),
          function (v) {
            return /^1[3456789]\d{9}$/.test(v) || `手机号码格式错误`;
          },
        ],
        email: [required("邮箱")],
      },
      passwordForm: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
      },
      passwordRules: {
        oldPassword: [required("旧密码")],
        newPassword: [],
        confirmPassword: [],
      },
    };
  },
  mounted() {
    this.initBaseInfo();
    this.initAccessLogs();
    this.initPasswordRules();
  },
  methods: {
    initBaseInfo() {
      this.userInfo = this.$store.state.user.userOnlineInfo;
      this.usercard = GeoPattern.generate(String(this.userInfo.userId), {}).toDataUrl();
      this.resetProfile();

      this.cardlines = [
        {
          icon: "mdi-email",
          text: this.userInfo.email,
        },
        {
          icon: "mdi-cellphone",
          text: this.userInfo.mobile,
        },
        {
          icon: "mdi-account-multiple",
          text: this.userInfo.organizations
              ? this.userInfo.organizations.map((item) => item.organName)
              : "无",
        },
        {
          icon: "mdi-account-tie-outline",
          text: this.userInfo.admin
              ? "超级管理员"
              : this.userInfo.roles
                  ? this.userInfo.roles.map((item) => item.roleName)
                  : "无",
        },
        {
          icon: "mdi-update",
          text: `加入于 ${this.$moment(this.userInfo.createdTime).format('YYYY-MM-DD')}`
        }
      ];
    },
    initAccessLogs() {
      getAccessLogsByUserId(this.userInfo.userId)
          .then((result) => {
            this.accessLogs = result.records;
          })
          .catch((err) => {
            console.warn(err);
          });
    },
    saveProfile() {
      if (this.$refs.form.validate()) {
        saveUserProfile(this.profileForm)
            .then(() => {
              this.$store.dispatch("user/resetCurrent", {
                ...this.userInfo,
                ...this.profileForm,
              });
              this.$toast.success("保存成功", {
                position: "top-center",
              });
            })
            .catch((err) => {
              this.$toast.error(err.message, {
                position: "top-center",
              });
              console.warn(err);
            });
      }
    },
    passwordUpate() {
      if (this.$refs.passwordForm.validate()) {
        updatePassword(this.passwordForm).catch((err) => {
          console.warn(err);
          this.$toast.success(err.message, {
            position: "top-center",
          });
        });
      }
    },
    resetProfile() {
      this.profileForm = {
        userId: this.userInfo.userId,
        username: this.userInfo.username,
        mobile: this.userInfo.mobile,
        email: this.userInfo.email,
        avatar: this.userInfo.avatar || "",
      };
    },
    checkNewPassword(passwordFormfield) {
      const _this = this;
      const getField = function () {
        return _this.passwordForm[passwordFormfield];
      };

      return function (v) {
        const field = getField();
        const checkResult = !field || v === field;

        if (_this.$refs.passwordForm) {
          const targetInput = _this.$refs.passwordForm.inputs.filter(
              (input) => input.id === passwordFormfield
          )[0];

          if (v === field && !targetInput.valid) {
            targetInput.resetValidation();
          }
        }

        return checkResult || `两次输入的密码不一致`;
      };
    },
    initPasswordRules() {
      this.passwordRules.newPassword = [
        required("新密码"),
        this.checkNewPassword("confirmPassword"),
      ];
      this.passwordRules.confirmPassword = [
        required("确认密码"),
        this.checkNewPassword("newPassword"),
      ];
    },
  },
};
</script>

<style>
.user-card-img {
  opacity: 0.8 !important;
  background-size: 100%;
}

.avatar-img {
  width: 170px;
  border-radius: 50%;
}
</style>
