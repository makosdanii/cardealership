/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

export default{
    setup() {
        return {}
    },
    props: ['player'],
    template: `<div class="card">
                    <div class="card-body">
                        <h6 class="card-title">
                            {{ player.name }}
                        </h6>
                        <p class="card-text">
                            <div>
                            {{ player.description }}
                            </div>
                        </p>
                    </div>
                </div>`
}

