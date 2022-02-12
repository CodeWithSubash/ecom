export default {
  methods: {
    getDate(date) {
      const monthNames = [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June',
        'July',
        'August',
        'September',
        'October',
        'November',
        'December',
      ]

      return {
        day: date.getDate(),
        month: monthNames[date.getMonth()].slice(0, 3),
        year: date.getFullYear(),
      }
    },
  },
}
