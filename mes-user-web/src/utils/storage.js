import Cookies from 'js-cookie';

const cookieStorage = {
    save: (name, value, exp) => {
        Cookies.set(name, value, {expires: exp});
    },
    remove(name) {
        Cookies.remove(name)
    },
    get(name) {
        console.log("cookie info: %o", Cookies.get(name));
        return Cookies.get(name)
    },
    clear() {
        Cookies.clear()
    },
};

export default cookieStorage
