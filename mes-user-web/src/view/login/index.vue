<template>
  <v-container class="container--fluid fill-height  sea-bg">
    <v-row align="center" justify="center" no-gutters>
      <v-col cols="12" lg="4" md="4" sm="8">
        <v-card class="elevation-10 pa-3 z-index-99 color-white bg-white">
          <div class="logo-mount"></div>
          <v-card-text>
            <div id="system-title" class="layout column align-center mb-6 ">
              <v-img :src="logoSrc" height="30" width="150" />
              <!--                                                        Citrus-->
            </div>
            <v-form @keydown.enter.native="login">
              <v-text-field
                v-model="model.loginId"
                clearable
                label="登录名"
                name="loginId"
                outlined
                placeholder="请输入登录名"
                prepend-icon="mdi-account"
                required
              />
              <v-text-field
                v-model="model.password"
                clearable
                label="密码"
                name="password"
                outlined
                placeholder="请输入密码"
                prepend-icon="mdi-lock"
                required
                type="password"
              />
            </v-form>
          </v-card-text>

          <v-card-actions>
            <v-spacer />
            <!--                        <v-btn color="primary" outlined to="/singup">注册</v-btn>-->
            <v-btn class="submit-btn" color="#acb6e5" dark @click="login"
              >登录</v-btn
            >
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
    <vue-threejs-birds
      :canvasBgAlpha="birds.canvasBgAlpha"
      :color1="birds.color1"
      :color2="birds.color2"
      :colorEffect="birds.colorEffect"
      :quantity="birds.quantity"
      :wingsSpan="birds.wingsSpan"
      style="max-width: none"
    />
    <!--消息提示-->
  </v-container>
</template>

<script>
import VueThreejsBirds from "vue-threejs-birds";
// import {API_BASE_PATH} from "../../config";

// const CAPTCHA_BASE_URL = API_BASE_PATH + "/rest/verify/captcha";

  export default {
    components: {
      VueThreejsBirds,
    },
    data: () => ({
      logoSrc: require("../../assets/text.png"),
      // captchaSrc: CAPTCHA_BASE_URL,
      model: {
        loginId: "",
        mode: "password",
        password: "",
        captcha: "",
      },
      birds: {
        color1: "#000000",
        color2: "#271e1e",
        quantity: 4,
        canvasBgAlpha: 0,
        wingsSpan: 20,
        colorEffect: 2,
      },
    }),
    methods: {
      handleResize() {
        this.$root.$emit("resized");
      },
      login() {
        this.$store
          .dispatch("user/login", this.model)
          .then(() => {
            this.$router.push("/");
          })
          .catch((err) => {
            console.warn(err);
            // this.refreshCaptcha();
            this.$toast.error(err.message, {
              position: "top-center",
            });
          });
      },
      // refreshCaptcha() {
      //   this.captchaSrc = CAPTCHA_BASE_URL + "?timestamp=" + Date.now();
      // },
    },
    mounted() {
      window.addEventListener("resize", this.handleResize);
    },
    beforeDestroy() {
      window.removeEventListener("resize", this.handleResize);
    },
  };
</script>

<style lang="stylus" scoped>
  .sea-bg
      background url(../../assets/cool-background.png) no-repeat;
      background-size cover

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  .color-white
      background-color #fff

  .captcha
      cursor pointer
      z-index 999
      position relative
      top -8px

  .submit-btn
      background #536976; /* fallback for old browsers */
      background -webkit-linear-gradient(to right, #292E49, #536976); /* Chrome 10-25, Safari 5.1-6 */
      background linear-gradient(to right, #292E49, #536976);

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  .bg-white
      background-color: rgba(255, 255, 255, 0.6) !important

  input::-webkit-input-placeholder
      color rgba(255, 255, 255, 0.1) !important

  .logo-mount
      position relative

  /*    content: '';
      position: absolute;
      z-index: 2;
      top: 60px;
      background-image: url("../../assets/logo.png");
      background-size: 100% 100%;
      left: 50px;
      width: 50px;
      height: 50px;
      border-radius: 2px;
      transform: rotate(45deg);
      -webkit-animation: box .8s infinite;
  */
  .logo-mount:before {
      content: '';
      position: absolute;
      z-index: 2;
      top: -30px;
      left: 10px;
      width: 50px;
      height: 50px;
      background-image: url("../../assets/logo.png");
      background-size: 100% 100%;
      border-radius: 2px;
      transform: rotate(45deg);
      -webkit-animation: box .8s infinite;
  }

  .logo-mount:after {
      content: '';
      position: absolute;
      z-index: 1;
      top: 48px;
      left: 12px;
      width: 44px;
      height: 3px;
      background: #eaeaea;
      border-radius: 100%;
      -webkit-animation: shadow .8s infinite;
  }

  //底部阴影动效
  @-webkit-keyframes shadow {
      0%, 100% {
          left: 14px;
          width: 40px;
          background: #eaeaea;
      }
      50% {
          top: 46px;
          left: 10px;
          width: 50px;
          height: 7px;
          background: #eee;
      }
  }

  //box跳动+旋转效果
  @-webkit-keyframes box {
      0% {
          top: -30px;
      }
      20% {
          border-radius: 2px;
      }
      50% {
          top: 0;
          border-bottom-right-radius: 25px;
      }
      80% {
          border-radius: 2px;
      }
      100% {
          top: -30px;
      }
  }
</style>
