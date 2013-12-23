Ext.onReady(function(){
/*
 *  Создаем форму для входа
 */
var loginForm = new Ext.FormPanel({
  url:'AuthorizationServlet',
  frame: true,
  items: [
      {
          xtype: 'textfield',
          id: 'login',
          fieldLabel: 'Имя',
          allowBlank:false,
          anchor: '90%'
      },{
          xtype: 'textfield',
          fieldLabel:'Пароль',
          name:'password',
          inputType:'password',
          anchor: '90%',
          allowBlank:false
      }
  ],

  buttons: [
      {
        text: 'Вход',
        handler: function() {         
            loginForm.getForm().submit({                 
                waitTitle: 'пожалуйста подождите...',
                waitMsg: 'вход в систему выполняется'
            });
            var login = loginForm.items.get(0);
            var pass = loginForm.items.get(1);
            Ext.Ajax.request({
                        url: 'AuthorizationServlet',
                        success: function(response){
                            if (response.responseText === "access done"){
                                window.location = 'maps.html';
                            }
                        },
                        failure: function () {
                            Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
                        },
                        method: 'POST',
                        params: { 
                                  username:login.getValue(), 
                                  pass: pass.getValue() 
                                }
                    });
        }
      }
  ]
});

/*
 *  Создаем окно, в которое помещаем форму
 */
var loginWindow = new Ext.Window({
    frame:true,
    title:'Вход в систему',
    width:330,
    closable: false,
    items: loginForm
});
 
loginWindow.show();

});
