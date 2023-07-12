
    var OneSignal = window.OneSignal || [];
    var initConfig = {
        appId: "3b757c90-5ba8-4f5e-bb2a-07e34d5ac90c",
        safari_web_id: "web.onesignal.auto.049ba086-b9bb-478c-827f-71a35c67ecc8",
        notifyButton: {
            enable: true,
        },
        allowLocalhostAsSecureOrigin: true,
    };
    OneSignal.push(function () {
        OneSignal.SERVICE_WORKER_PARAM = { scope: '/src/onesignal/' };
        OneSignal.SERVICE_WORKER_PATH = 'src/onesignal/OneSignalSDKWorker.js'
        OneSignal.SERVICE_WORKER_UPDATER_PATH = 'src/onesignal/OneSignalSDKWorker.js'
        OneSignal.init(initConfig);
    });