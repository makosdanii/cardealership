/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

export default {
  emits: ['response'],
  setup(props, { emit }) {
    emit('response', 'hello from child')
    return {}
  },
  template: `
  <h2>Child component</h2>
  `
}