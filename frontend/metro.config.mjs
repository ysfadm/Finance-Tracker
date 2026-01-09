export default {
  project: {
    ios: {},
    android: {},
    windows: null,
    macos: null,
  },
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: false,
      },
    }),
  },
};
