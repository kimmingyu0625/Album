module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}", // Tailwind가 CSS를 적용할 파일 경로
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Open Sans', 'sans-serif'],
        serif: ['Lora', 'serif'],
      },
    },
  },
  plugins: [],
};