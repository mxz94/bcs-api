<template>
  <div style=" background-color: #f0f0f0;  min-height: 100vh; ">
    <van-image
        src="/images/banner.png"
    />
    <van-notice-bar v-if="user.noApplyMonth > 0" :scrollable="true" color="#1989fa" background="#ecf9ff" style="position: absolute; top: 0%; width: 100%">
      您已经累计{{user.noApplyMonth}}个月未开单啦！
    </van-notice-bar>

    <div class="article-content  w-full p-[20px]"  v-if="user.userId != null">
      <div class="p-[20px] rounded-[10px] text-white personal-data" >
        <van-row class="flex flex-wrap">
          <van-col span="16">
            <div class="flex flex-col">
                <div class="flex items-center py-[5px]">
                  <van-image :src="user.avatar" width="60px" height="60px" round="true"/>
                  <div span="18" class="flex-1 ml-[13px]">
                    <div  class="text-[18px] font-bold">{{ user.userType_dictText }}</div><div class="text-[12px]">
                    {{ user.nickName }}(ID:{{user.userId}})</div><div data-v-cb07a6e7="" class="text-[12px]">推荐人: {{user.fromUserNickName || '无'}}</div>
                  </div>
                </div>
              <div class="flex flex-row pt-[10px]">
                  <div class="flex-1 mr-[5px]">
                    <div class="text-[25px] font-bold">{{ user.allBalance  || 0 }}</div>
                    <div class="text-[13px]">现金</div>
                  </div>
                <div class="flex-1 ml-[5px]">
                  <div class="text-[25px] font-bold">{{ user.callBalance  || 0 }}</div>
                  <div class="text-[13px]">话费分成({{ user.huafeiTeamTotalRate || 16 }}%)</div>
                </div>
              </div>
            </div>
          </van-col>
          <van-col span="8">
            <div class="flex flex-col items-center h-full">
              <van-image
                  src="/images/5G.png"
              />
              <div class="flex-1 flex items-center text-[#01479D] text-[15px] font-bold" @click="onClickShare">
               <div class="bg-white rounded-[20px] mt-[10px] px-[15px] py-[5px]">
                 分享码
               </div>
              </div>
              <div class="van-overlay" v-if="show">
                <div class="h-[100%] flex justify-center items-center p-[20px]">
                  <div class="w-[100%] bg-white rounded-[10px] text-[#01479D] text-center px-[20px] py-[20px] overlay-content qr_bg">
                    <div class="font-bold text-[20px]">
                      <van-image :src="user.avatar" width="60px" height="60px" round="true"/>
                    </div>
                    <div class="text-white text-[15px] member-name rounded-[20px] inline-block px-[10px] py-[5px] mb-[40px]">
                      {{ user.nickName }}
                    </div>
                    <div class="flex justify-center items-center mt-[160px]">
                        <img :src="qrCodeUrl" alt="QR Code" />
                    </div>
                    <van-icon name="cross" class="van-badge__wrapper close-icon" @click="closeShare"></van-icon>
                  </div>
                </div>

              </div>
            </div>
          </van-col>
        </van-row>
      </div>
      <div class="mt-[20px] px-[10px] py-[20px] grid grid-cols-3 place-content-evenly bg-white rounded-[10px] text-[#000000]">
        <div class="flex flex-col text-center" @click="goPage(1)">
          <div class="flex justify-center items-center w-[100%]">
            <van-image  src="/images/1.png" width="8vmin" height="8vmin"/>
          </div>
          <div class="text-[13px] pt-[10px]" >
            账单记录
          </div>
        </div>
        <div class="flex flex-col text-center" @click="goPage(2)">
          <div class="flex justify-center items-center w-[100%]">
            <van-image  src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGkAAABpCAYAAAA5gg06AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBQjUwMDk0MjU5MzcxMUVGQjkzMEQ2NkU2NTYyQ0EwNiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBQjUwMDk0MzU5MzcxMUVGQjkzMEQ2NkU2NTYyQ0EwNiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkFCNTAwOTQwNTkzNzExRUZCOTMwRDY2RTY1NjJDQTA2IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkFCNTAwOTQxNTkzNzExRUZCOTMwRDY2RTY1NjJDQTA2Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+47wMzgAACDVJREFUeNrsnUF22zYQ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" width="8vmin" height="8vmin"/>
          </div>
          <div class="text-[13px] pt-[10px]">
            现金提现
          </div>
        </div>
        <div class="flex flex-col text-center" @click="goPage(3)">
          <div class="flex justify-center items-center w-[100%]">
            <van-image  src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGkAAABpCAYAAAA5gg06AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCQkUzMzBFQTU5MzcxMUVGOUVFRUM3RDA4RjYzQjFFNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCQkUzMzBFQjU5MzcxMUVGOUVFRUM3RDA4RjYzQjFFNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkJCRTMzMEU4NTkzNzExRUY5RUVFQzdEMDhGNjNCMUU0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkJCRTMzMEU5NTkzNzExRUY5RUVFQzdEMDhGNjNCMUU0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+OEzNJgAABV5JREFUeNrsnE1rFDEYxydrLaLWk9AXqNBDFSyKBb21dHuqpXi0Nz1sEcVvIBS0UNC7IKK0H0IptqducfWkrShVevKkHpeKq5e1cbLva5NMMpPJ7Gb+fxgLM5txJr/keZ48SYZQSj2os0UACZAgQAIkCJAg45AIIV6P52XTXFllz8ubvJ9y3av+8Cghm17KIfmaNglKte4zGvdMO6DE6iCDeu98ARIgQYCUEvUYus+SY/Vyv6OehoWBKodPkwqOB4612qytd1Wte5g7+CQIkAAJci2642pxcXFEdG15efmrbhnTEj1DxynO6O7y+Pg+r8zFsbFfojJTExM/JP+XsePunTsPEd1B8EmABAESBEgIwXU0Ojr63j+41z7u7nLPDw0Nfbk+P/8FaCyF4F3aahGCQ/BJgAQBEgRICMG5YgnWvb29U/+fHxkZKfkh+EleGZZg3d7eHoj7xW/cvPnoydOn91IPienP799WysDcQYAEARIgQV0eOPQPDHybmZ39xrsmSrCe7Osr+mWKQNMiJFgPtVokWCH4JECCAAkCJITgopu/9v8c410re94V3nmWYC0Wi31xv/jE5ORjXoK19gkAkhpINV3mnHsnK/B5d/eEBUgwdxAgARIESFAHBg4F3QKXxsd/Ak1TOl/pEv1wqexCkvXq82y1Qo60fMOBZmvVlK/8S+hW/crB+kLkd47jU2ruQfLBNKHUgWi0cFL9yEhYYICkBEcfjAyYLixAsgTnv+rMM5OoCiuOjxJ2vc8hNLMZH6CqyfTr/X7m6orRRpsKSKzSqoDsyDSoWM0dS5aK7rdVKAzqlmnVubNnXzxbXb2tAohVmq7ZavoaukUpmQoXXJA8JX+XvPVb+SjmLtZxUqlUOv5hZ+fQMuPzY2MlWbk3hcKAAiTPLCB+hVKO2cx4mazafSnzgeweeZi7SIAYnINpupGbFrX4Nvm/YYEB3Vgg9RA8ENTM6iYgcVp7MCBNOBypw6LZKD7KSUjBQYIPKAIcHqwgUJVGU8tqpB5SUItllVkBZFgqoNpTTuqKPcE6PDxctlGmrcVKetDBei62gTcD5TcSyTP40SHrTZo92KmMQ1CwwPxH8s9RM7VpzTjIAKlEYmwZsX9sco6sbo+SBRG6vqnHGULsxWnYimtIBJlNUeiZKOb7BI2G+SadsZMzPYkNMKP0ojj8k7l3c8bU1VM38VaY5mAgb8LkOeST+Hm1JHpR44lYmilEz7fqk67NzX0SXXu5tnZBt0yrBvv73zYSrAH+KDGxUHtmJfJtYoX04/v3M7oJ1p/7+6dVEqy5XO6tkm/wDkxkFaZsmmcnzZ2O6Qgp4e6/8H4phYGD1OR0fSN0QFKTFjKpCUipkijqbK7hSzzj0Nvba6WMy3ImwUpmVqjIcatOTagFAu0qyzacNVYo8cdvf1+pZeR70E7bxGAa82GVtRDoSa09aVW4po5NkycR5UmfaWOBpG6qQpaCCTsjGqkXVWaIzaSq3InupD1Ffw7HZkbBBqSpziElHt3b7E2yXlQZ12lm5U34JCYr9r5cdexemGiqbmZin7bQeAabuyqsqazwfQWZs7YBSjwUaAYMTZOY0l0VsgCiWjHmdz00elDAStWwc1vO9aS6TwhawWq0RwWYONGgOrXmThdUGEeuY155Zg6QQlSgNiytnRXiwXQckB4kHW4HRnehzJDYb7ROgVSh6O9TkplV45DY1oFulWqPMh/EyNNR2DPb5g9y03ZXDVW31ZjKF6aiJ+kEE0YAKU6NwNxZhyXfHwtIicLShwNIIQejOiF1FDCAZBjcoXOGJw6NQ4ISHEIAEiBBgARIECBBqvonwAC/by4sVToLIwAAAABJRU5ErkJggg==" width="8vmin" height="8vmin"/>
          </div>
          <div class="text-[13px] pt-[10px]">
            现金提现记录
          </div>
        </div>
        <div class="flex flex-col text-center py-[20px]" @click="goPage(4)">
          <div class="flex justify-center items-center w-[100%]">
            <van-image  src="/images/4.png" width="8vmin" height="8vmin"/>
          </div>
          <div class="text-[13px] pt-[10px]">
            话费分成记录
          </div>
        </div>
        <div class="flex flex-col text-center py-[20px]"@click="goPage(5)">
          <div class="flex justify-center items-center w-[100%]">
            <van-image  src="/images/5.png" width="8vmin" height="8vmin"/>
          </div>
          <div class="text-[13px] pt-[10px]">
            话费分成提现
          </div>
        </div>
        <div class="flex flex-col text-center py-[20px]" @click="goPage(6)">
          <div class="flex justify-center items-center w-[100%]">
            <van-image  src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGkAAABpCAYAAAA5gg06AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo4MEZERTEzODU5MzcxMUVGQkQwRUY4NzRENDkxOUE0NyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo4MEZERTEzOTU5MzcxMUVGQkQwRUY4NzRENDkxOUE0NyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjgwRkRFMTM2NTkzNzExRUZCRDBFRjg3NEQ0OTE5QTQ3IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjgwRkRFMTM3NTkzNzExRUZCRDBFRjg3NEQ0OTE5QTQ3Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+IRNjOwAACFFJREFUeNrsnb9vHEUU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" width="8vmin" height="8vmin"/>
          </div>
          <div class="text-[13px] pt-[10px]">
            话费分成提现记录
          </div>
        </div>
        <div class="flex flex-col text-center" @click="goPage(7)">
          <div class="flex justify-center items-center w-[100%]">
            <van-image  src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGkAAABpCAYAAAA5gg06AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDoxRjVENTA1RjU5MzcxMUVGQjIzM0YxNjc4QkFCOTAxRCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDoxRjVENTA2MDU5MzcxMUVGQjIzM0YxNjc4QkFCOTAxRCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjFGNUQ1MDVENTkzNzExRUZCMjMzRjE2NzhCQUI5MDFEIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjFGNUQ1MDVFNTkzNzExRUZCMjMzRjE2NzhCQUI5MDFEIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+ABUDgQAABP9JREFUeNrsnVty2zAMRUnV+zL9lawjma7D1Do66TqSL9EbMxtNrEZ1ZYsgABKUgRlNWqW1KR5evPSyMUajJts6nQKFpKaQFJKaENtd77DWSh6vm/3cL+y/tjD782m2L0g9wMVEbtw53wRCGS5bZNj8ZRMF6T8mAiE5Rij3tkECMMmQaoERpzCJkKTBuQXsISG1AKcKLAmQWoRTFFZtSL5xOEVA1YK0BfUUg1UDktsoHDZQS5DsNRjCjsM4+CODMqduQX+js7C0UOYdCa4x9RSdjJIdB0r3NnUcHEOMpI6TrhV3NxDCcaaMecJxe+mQhobAcKrLS4U0mIYq+wIlg5cGadgQnKqguCD5Rl1bKViuNiS/QfVUBUUN6ZEAYV37UAOSM5XqiIZV5UtDGh4YEAaUKwXJKyDUXLBDcgoI7VUGKKRdRlcbYiRNxyR7/rU8tvef3N9/AJYTU8M3eVyQLrgHdpB7lkzuAsPGH5exxMTJscHaeDqbc2AA5yAZ3AXQ4ZaSMJAixSAwcL7AREfxcZ+H2Z/fXykXEXQRH5bUhIEEHYCVCocZFsTtLS5kDCSIisjcXPf85j+Hx3GSjhNUxKhpCVKXKGOo7JsB9DUx5miffg9EHwdx80nHR31XRd8aoBkqRwQqADI3l+IeU9xdBADybQIid32QbO+f2JTj7nzpSaoJ6O/336q5+NSEcndHwKBIVCShhfBdgxVz/a5ETDptQUXz+FRYTcdcSJBVLURFNnxvItREoqR7vbt9yYwOCyd+vBxokxCS4rk3sJ5egCrJlZzm3Mkcs7ElQKONWdr4+4qrhySB6AgA4d0Uwv+vpcuodBoflyC254IUKEbXmY55MmyoqKaeS0lFszr+yc+MLzSnNAJ+EeOSBtJKn2PFC6m9UHFpJ8HdLcaNz3gwucEY7X5JDeNJvLgSU1AJiYyiDXz6nAXSLVdzXvr8Gby1oG9jNwiZ51Piwl9Mw3emNbsFjxgQ8Vlb8pjkqqsIGYOwgBhqq9S52nNkd+IAYft/DNc+sChJAQmznQL6rrnO7y9e5PEpoPv9v9Yhua0AEuTiTptTUkOAHLWSQguAsO0eqUkCh5JcLUAYFUV7PrQC6B6kYDZqI6ACd1okFamp895kTMI0TSsAYolJEDu2AraRGARSUi/2MDJPaVcE5DGAqBIHZ9TYaiSqxEG8y6t8xRB6froc+amxJAw+BxLkKpdyLi8jO6sYj1Ln5e4xNdoFt62oPNXVnVZqjrvPcUh9RsFg1JbcF/iRNjkP23BGH6qRXXObjKekUDxs456Nairy8iX79Aa62ip+vNoKKkq11di/VicFYJbnVUDgtDtgIUG7D0flA1YRCSSomgZVEa11AOKQ2sCpiuj+fSokVVPahEMfkEWqJKia1O2tz6XngARVk3uwbI8taeoyVgB04JqWI1SUAylkgtJuBKKW7DK/JAD/z6CKyvJE2ZByv0xdX+45On3CPpmJei64gmoM0hRvosKSDUlBNQIJC2oLsJqARAGqZVjNQKICNcHyConvjc3e0L8V2T8qpFbf2Hy6+nsQAmnNDmtjLfnGZi5Ftb5lKYn7/qQRkjWS79JowErdROYVlHxIqqpGICmshiAprIYgKaxE46yTMNCM2ebVsKuTW6NOwpq7QKNqNdXckjompTsOnOCmonA/2yfVpg5JMqRVd6emiYOaQlJIagpJ7Zb9EWAAwZtqVp8j0xoAAAAASUVORK5CYII=" width="8vmin" height="8vmin"/>
          </div>
          <div class="text-[13px] pt-[10px]">
            常见问题
          </div>
        </div>
        <div class="flex flex-col text-center"@click="goPage(8)">
          <div class="flex justify-center items-center w-[100%]">
            <van-image  src="/images/8.png" width="8vmin" height="8vmin"/>
          </div>
          <div class="text-[13px] pt-[10px]">
            我的团队
          </div>
        </div>
<!--        <div class="flex flex-col text-center"@click="lougout()">-->
<!--          <div class="flex justify-center items-center w-[100%]">-->
<!--            <van-image  src="/images/8.png" width="8vmin" height="8vmin"/>-->
<!--          </div>-->
<!--          <div class="text-[13px] pt-[10px]">-->
<!--            退出(开发)-->
<!--          </div>-->
<!--        </div>-->
      </div>
    </div>
  </div>
</template>

<script>

import {Toast} from "vant";
import QRCode from 'qrcode'
import {deleteToken, getToken} from "@/libs/util";
export default {
  data() {
    return {
      show: false,
      user: {
        "userId": null,
        "avatar": "",
        "nickName": "zxy",
        "balance": 0.00,
        "waitInBalance": 0.00,
        "callBalance": 0.00,
        "noApplyMonth": 0,
        "qianfei": 0,
        "fromUserNickName": null,
        "shareUrl": "",
        "allBalance": 0.00,
        "userType_dictText": "管理员",
        "qianfei_dictText": "否",
        "huafeiTeamTotalRate": 0.00
      },
      qrCodeUrl: ''
    };
  },
  created() {
      var token = getToken();
      if (!token) {
        this.$router.push('/login');
        return;
      }
      let that =this
      this.$request(
          "/wechat/userInfo",
          "get",
          null,
          null,
          function (res) {
            console.log(res)
            if (res.code == 200) {
              that.user = res.data
            } else {
              that.$router.push('/login');
              deleteToken()
            }
            // that.$storage.delData("wxcode");
          }
      );
  },
  methods: {
    lougout() {
      deleteToken()
      this.$router.push('/login');
    },
    goPage(id, event) {
      switch (id) {
        case 1:
          this.$router.push('yjrecord');
          break;
        case 2:
          this.$router.push('yjtx');
          break;
        case 3:
          this.$router.push('yjtixian');
          break;
        case 4:
          this.$router.push('hfrecord');
          break;
        case 5:
          this.$router.push('hftx');
          break;
        case 6:
          this.$router.push('hftixian');
          break;
        case 7:
          this.$router.push('question');
          break;
        case 8:
          this.$router.push('team');
          break;
        default:
          console.log('Unhandled ID:', id);
      }
    },
    onClickShare() {
      this.generateQRCode()
      this.show = true
    },
    closeShare() {
      this.show = false
    },
    generateQRCode() {
      if (this.user.shareUrl) {
        QRCode.toDataURL(this.user.shareUrl, { width: 140, margin: 1 }, (err, url) => {
          if (err) {
            console.error(err)
          } else {
            this.qrCodeUrl = url
          }
        })
      }
    }
  }
};
</script>

<style lang="less">
.overlay-content {
  position: relative
}

.close-icon {
  position: absolute;
  top: 5.3333vmin;
  right: 5.3333vmin;
  color: rgb(168, 168, 168);
  font-size: 25px;
}

.member-name {
  background-color: #01479d
}

.qr_bg {
  background-image: url("/images/qr_bg.jpeg");
  background-repeat: no-repeat;
  background-size: cover
}
.van-overlay {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, .7);
}
.personal-data {
  background-color: #01479d;
}
.article-content {
  position: absolute;
  top: 4%;
  left: 0;
}
</style>
