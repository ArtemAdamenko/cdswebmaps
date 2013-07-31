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
          fieldLabel: 'Login',
          allowBlank:false,
          anchor: '90%'
      },{
          xtype: 'textfield',
          fieldLabel:'Password',
          name:'password',
          inputType:'password',
          anchor: '90%',
          allowBlank:false
      }
  ],

  buttons: [
      {
        text: 'Login',
        handler: function() {
            loginForm.getForm().submit({
                waitTitle: 'пожалуйста подождите...',
                waitMsg: 'вход в систему выполняется'
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
