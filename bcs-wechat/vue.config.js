module.exports = {
  outputDir: 'dist',
  publicPath: process.env.NODE_ENV === 'production' ? '/' : '/',
    css: {
        loaderOptions: {
            postcss: {
                plugins: [
                    require('tailwindcss'),
                    require('autoprefixer'),
                ],
            },
        },
    },
};
