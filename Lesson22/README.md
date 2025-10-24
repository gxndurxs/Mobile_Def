#Практическая работа №2

Тема: Жизненный цикл Activity, переходы между экранами, диалоги и уведомления в Android-приложениях
Цель практической работы: Укрепить навыки в следующих областях:

использование инструментов отладки в Android Studio;
понимание этапов жизненного цикла Activity и методов сохранения состояния;
работа с Intent (явными и неявными) для взаимодействия между экранами и приложениями;
вывод информации пользователю при помощи Toast, Notification и DialogFragment.

Отладка приложений

В Android Studio был изучен режим отладки Debug, который дает возможность пошагового выполнения программы, установки точек останова и наблюдения за переменными. Также применялся Android Profiler для мониторинга работы CPU, использования памяти и сетевой активности в реальном времени. Через Logcat выводились диагностические сообщения с использованием Log.d(TAG, "сообщение") и других уровней: Debug, Info, Warning, Error.

Жизненный цикл Actitvity
Был создан модуль ru.mirea.OstrovskiyAI.lesson2 с модулем ActivityLifecycle. В MainActivity переопределены все ключевые методы жизненного цикла: onCreate, onStart, onResume, onPause, onStop, onDestroy, onRestart, onSaveInstanceState, onRestoreInstanceState. Добавлен элемент EditText в activity_main.xml. В каждом методе логгирование состояния через Log.i(TAG, "onStart()") и другие сообщения.

<img width="1879" height="560" alt="image" src="https://github.com/user-attachments/assets/9c3998cc-de73-42bd-aa67-07b20dcdf38d" />

Ответы на вопросы:

Будет ли вызван метод onCreate после нажатия на кнопку Home и возврата в приложение? Нет, будет вызван onRestart(), затем onStart() и onResume(), но не onCreate(), так как активность не уничтожалась.

Изменится ли значение поля EditText после нажатия на кнопку Home и возврата в приложение? Нет, не изменится. Значение сохранится, так как активность не была уничтожена.

Изменится ли значение поля EditText после нажатия на кнопку Back и возврата в приложение? Да, изменится. Активность уничтожается (onDestroy()), и при повторном запуске вызывается onCreate(), поле сбрасывается в исходное состояние.

Создан модуль MultiActivity, в котором реализована вторая активность SecondActivity. В MainActivity добавлена кнопка, по нажатию на которую срабатывает переход на SecondActivity с помощью явного Intent. Также реализовано поле EditText и кнопка «Отправить». Введённый текст передаётся через intent.putExtra() и отображается во втором окне в TextView.

<img width="1472" height="950" alt="image" src="https://github.com/user-attachments/assets/3cae9341-e99d-4491-b798-2d7c104ae399" />
<img width="374" height="833" alt="image" src="https://github.com/user-attachments/assets/75fecff6-948d-496f-8f69-28ca3a3bd585" />

Неявные намерения

В модуле IntentFilter реализованы 2 кнопки. В MainActivity реализована кнопка, открывающая браузер по ссылке https://www.mirea.ru/ с использованием Intent.ACTION_VIEW. Вторая кнопка, выполняющая передачу ФИО с помощью Intent.ACTION_SEND и Intent.EXTRA_TEXT.

<img width="363" height="843" alt="image" src="https://github.com/user-attachments/assets/829e0cde-94d4-41f9-b654-9ad472bee0a4" />

<img width="368" height="846" alt="image" src="https://github.com/user-attachments/assets/db434277-702f-4f8f-9c6d-ab4df9353d46" />

Диалоговые окна

Toast - уведомления: В модуле ToastApp добавлено поле EditText и кнопка. При нажатии отображается сообщение (Toast) с количеством введённых символов.

<img width="367" height="776" alt="image" src="https://github.com/user-attachments/assets/b36518bf-d519-45ee-91aa-1f73bb69f66b" />

Notification

Создан модуль NotificationApp, в котором реализована кнопка, по нажатию на которую появляется системное уведомление.

<img width="357" height="821" alt="image" src="https://github.com/user-attachments/assets/34eaa6b4-affb-4eec-8ce1-ff8d664c196a" />

<img width="357" height="793" alt="image" src="https://github.com/user-attachments/assets/7367ad4d-ca20-4ff4-8d51-188f7ec19822" />

Диалоговые окна

В модуле Dialog реализован AlertDialogFragment, наследующий DialogFragment. В MainActivity реализован диалог с тремя вариантами выбора: «Иду дальше», «На паузе», «Нет». В зависимости от выбора выводится соответствующее сообщение.

<img width="719" height="1004" alt="image" src="https://github.com/user-attachments/assets/8f5a46bc-af7f-451e-8c19-00ac6bfaa430" />

<img width="359" height="858" alt="image" src="https://github.com/user-attachments/assets/2ddbb4b4-3de8-41be-a5a6-560aca550654" />

<img width="364" height="841" alt="image" src="https://github.com/user-attachments/assets/ad5e44b2-3d88-4140-92dd-372e6f581c91" />

Самостоятельная работа

Были реализованы классы: MyTimeDialogFragment, MyDateDialogFragment, MyProgressDialogFragment. В разметке добавлены 3 кнопки, каждая кнопка открыват соответствующее диалоговое окно:

Show Time Picker
Show Date Picker 
Show Progress Dialog

<img width="359" height="849" alt="image" src="https://github.com/user-attachments/assets/1a9110d0-0636-4c07-a7ae-5cb2eea8103d" />

<img width="371" height="842" alt="image" src="https://github.com/user-attachments/assets/97ca8fc4-0518-4040-8109-5f0dafff65f7" />

<img width="386" height="851" alt="image" src="https://github.com/user-attachments/assets/a81666dd-8efb-451a-94ea-e3db0ad92981" />

Заключение

В ходе выполнения данной практической работы были освоены:

средства отладки и логирования в Android Studio;
основные этапы жизненного цикла Activity;
реализация переходов между экранами с помощью Intent;
вывод информации пользователю с помощью Toast, Notification и различных диалогов;
базовые принципы работы с интерфейсом и навигацией между активностями.


Работу выполнил: Островский А.И
Группа: БСБО-09-22
