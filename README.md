**Описание приложения**

Android-приложение демонстрирующее работу с Yandex mapkit и CameraX.

Приложение имеет 3 вкладки и нижнюю панель навигации. Первая вкладка - галерея изображений, галерея наполняется с помощью создания фотографий на втором фрагменте. 

Вторая вкладка предназначена для работы с камерой через библиотеку СameraX. 

На третьей вкладке расположена карта с размещенными на ней маркерами. С помощью маркеров я указал на карате события, которые загружаются с API-сервиса KudaGo.com (Я взял события относящиеся к категории education).

На экране есть кнопки масштабирования и перехода к текущему местоположению.
В качестве SDK карт я использовал Yandex mapkit. При нажатии на маркер открывается alertDialog в качестве информационного окна с одной кнопкой.
Информация в alertDialog содержит данные: название места в котором запланировано мероприятие и подробная информация о нем.

К проекту приложены unit-тесты для основных usecases.

<img src="https://github.com/AlekseyFokin/YandexMap-KudaGo/blob/main/map2.gif" alt="gif" width="360" height="800">

**Инструкция по сборке**

Шаг 1: На корневой странице этого репозитория (https://github.com/AlekseyFokin/Skillcinema_coursework_android) на середине страницы раскройте выпадающие меню на зеленой кнопке с надписью "<> Code" и скопируйте в буфер обмена url репозитория.

Шаг 2: Откройте Android Studio, затем перейдите в меню Файл > Создать > Проект из системы управления версиями.

Шаг 3: В поле Version control выберите Git из выпадающего меню.

Шаг 4: Вставьте url из буфера обмена в поле URL и выберите свой каталог, в котором будет размещена ваша копия проекта. Нажмите кнопку "Клонировать".

Шаг 5: В Android Studio запустите команду Build > Make Project. Дождитесь окончания подключения всех зависимостей.

Шаг 6: В Android Studio выберите конфигурацию запуска приложения (обычно app) или создайте свою. Произведите запуск приложения.


  


