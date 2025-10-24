# Практическая работа №3
**Тема:** Намерения, обмен данными, вывозы системных приложений и работы с фрагментами в приложения на базе android.  
**Цель работы**
В данной практике была поставлена задача закрепить навыки:
- работы с явными и неявными Intent;
- передачи и получения информации между экранами;
- интеграции встроенных приложений Android (звонки, карты, браузер);
- создания фрагментов с учетом ориентации устройства;
- применения Activity Result API для обмена результатами.
## Работа с намерениями
Создал модуль ItentApp. В нем реализовал пару активностей.
1.	В MainActivity получено текущее системное время и передано во вторую активность (SecondActivity) с помощью Intent.
2.	В SecondActivity отображается текст: “Квадрат значения моего номера по списку в группе составляет число, а текущее время время”.
!<img width="1523" height="966" alt="image" src="https://github.com/user-attachments/assets/7c6eab4c-9900-467b-87b5-ccc0a6945443" />
!<img width="385" height="812" alt="image" src="https://github.com/user-attachments/assets/8ac967f8-dc0a-40d5-aff9-d50d3b7e92ce" />


## Обмен данными и получение результата
Реализован механизм двустороннего обмена данными с помощью Activity Result API. Главная активность (MainActivity) содержит кнопку, которая запускает вторую активность (ShareActivity). На втором экране пользователь вводит название своей любимой книги и цитату. После нажатия кнопки "Отправить" эти данные упаковываются в Intent и возвращаются в MainActivity как результат. MainActivity получает этот результат через ActivityResultCallback и обновляет свой TextView, отображая введенные пользователем данные.
!<img width="731" height="990" alt="image" src="https://github.com/user-attachments/assets/140edf8f-c954-48ab-a2ad-36369084f5ef" />

## Экран ввода данных:
!<img width="376" height="849" alt="image" src="https://github.com/user-attachments/assets/c42bd361-89e7-4150-925e-129290b45e2e" />

## Главный экран после получения результата:
!<img width="387" height="838" alt="image" src="https://github.com/user-attachments/assets/1769e9c7-ee2a-428c-8b59-84e6d85e8292" />

## Вызов системных приложений
Создано приложение с тремя кнопками для демонстрации вызова системных приложений с помощью неявных Intent.

«Позвонить»: Использует Intent.ACTION_DIAL для открытия системного приложения "Телефон" с заранее введенным номером.

«Открыть браузер»: Использует Intent.ACTION_VIEW и Uri со схемой http:// для открытия веб-страницы в браузере.

«Открыть карту»: Использует Intent.ACTION_VIEW и Uri со схемой geo: для открытия приложения с картами и отображения точки по заданным координатам.

!<img width="788" height="1031" alt="image" src="https://github.com/user-attachments/assets/38d8cedb-0c1b-4155-bcf4-ac0783ae0057" />
!<img width="352" height="817" alt="image" src="https://github.com/user-attachments/assets/966f7d34-f003-42da-9653-3a9b6f06f948" />
!<img width="373" height="844" alt="image" src="https://github.com/user-attachments/assets/8256da33-77c2-4b31-a6bd-bda8ce71918c" />
!<img width="357" height="842" alt="image" src="https://github.com/user-attachments/assets/7901485a-2930-4803-bd1a-43b15617ce56" />

## Фрагменты
Было создано приложение на основе Fragment. В MainActivity размещены две кнопки для переключения между двумя разными фрагментами (FirstFragment и SecondFragment) в портретной ориентации. Для альбомной ориентации был создан альтернативный файл разметки (res/layout-land/activity_main.xml), в котором оба фрагмента отображаются на экране одновременно, занимая разные его части. Логика переключения реализована с помощью FragmentManager и FragmentTransaction.

!<img width="371" height="818" alt="image" src="https://github.com/user-attachments/assets/d4edc484-017b-4009-ab0e-881b70c05ece" />
!<img width="359" height="831" alt="image" src="https://github.com/user-attachments/assets/2f7a3825-a171-4025-accd-813e2ddcb05a" />
!<img width="362" height="817" alt="image" src="https://github.com/user-attachments/assets/ceae8eeb-8943-4c65-9379-be22152f1aad" />
!<img width="1016" height="886" alt="image" src="https://github.com/user-attachments/assets/59767f4c-bef6-468e-8f67-d45de378cb58" />

## Итог практической работы
По результатам выполнения задания были успешно освоены:

создание Intent и передача информации между экранами;
использование Activity Result API для возврата данных;
вызов встроенных приложений Android (телефон, браузер, карты);
построение и переключение фрагментов, в том числе с учётом ориентации экрана.

## Дополнительный проект: MireaProject

В рамках самостоятельной части был разработан проект MireaProject на основе шаблона Navigation Drawer Activity. Проект включает два фрагмента: • DataFragment, содержащий сведения о отрасли - Веб-разработка: где мы сегодня и куда движемся; • WebViewFragment, в котором реализован встроенный браузер для открытия сайта Mirea Навигация реализована через: • редактирование файла mobile_navigation.xml, куда были добавлены фрагменты; • настройку меню в activity_main_drawer.xml, в котором добавлены пункты: Информация и Браузер, с соответствующими иконками.
!<img width="484" height="962" alt="image" src="https://github.com/user-attachments/assets/3e2a7c96-a402-4525-b8ba-a7bd7de72628" />
!<img width="665" height="929" alt="image" src="https://github.com/user-attachments/assets/977f6f3d-413f-4f5c-8402-68a9f240a3e3" />
!<img width="686" height="996" alt="image" src="https://github.com/user-attachments/assets/a7208ba1-0e48-4a14-bc89-2db30f6dff52" />


--- 

**Работу выполнил**: Островский А.И 
**Группа**: БСБО-09-22



