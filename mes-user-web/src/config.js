export const API_BASE_PATH =
    // 'http://localhost:9002';
    process.env.NODE_ENV === "production" || process.env.NODE_ENV === "test"
        ? process.env.BASE_URL
        : "/gateway";

/**
 * 系统配置
 * @type {{systemTile: string}}
 */
export const SYSTEM_CONFIG = {
  appBarBgSrc: {},
  appBarColor: "#fff",
  //面包屑导航
  breadcrumb: true,
  menuDark: false,
  menuBgSrc: require("./assets/wave.svg"),
  menuLogoSrc: require("./assets/logo.png"),
  menuTitle: "Citrus",
  menuColor: "#fff",
  menuTextColor: "#204e97",
  // systemTitle: "广大家具",

  // 标题在主要工具拦
  toolbarTitle: false,
  systemImageSrc: require("./assets/text.png"),
  // systemImageSrc: "",

  /**
   * 允许不需要权限访问的路由
   */
  permitUrls: ["/login"],
};

export const DEFAULT_ACCOUNT = {
  loginId: null,
  password: null
};

export const CLIENT_INFO = {
  client_id: 'gndbiz',
  client_secret: 'gndbiz1101'
};

// oAuth Client secret
// export const CLIENT_SECRET = process.env.CLIENT_SECRET || atob(`${CLIENT_INFO.client_secret}=${CLIENT_INFO.client_secret}`);