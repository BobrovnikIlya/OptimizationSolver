function decreaseValue() {
    var input = document.getElementById('myInput');
    var currentValue = parseFloat(input.value);
    var newValue = currentValue - currentValue * 0.1;
    input.value = newValue.toFixed(2); // Задаем округленное значение до двух знаков после запятой
}

function increaseValue() {
    var input = document.getElementById('myInput');
    var currentValue = parseFloat(input.value);
    var newValue = currentValue + currentValue * 0.1;
    input.value = newValue.toFixed(2); // Задаем округленное значение до двух знаков после запятой
}