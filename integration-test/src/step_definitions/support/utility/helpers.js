function addDays(days) {
    const date = new Date();
    date.setDate(date.getDate() + days);
    return date;
}

function formatWithValidYear(dateToFormat) {
    const date = new Date(dateToFormat);
    return format(new Date(date.setFullYear(date.getFullYear() + 1)));
}

function format(dateToFormat) {
    let dd, MM, yyyy;
    dd = dateToFormat.getDate().toString().padStart(2, '0');
    MM = (dateToFormat.getMonth() + 1).toString().padStart(2, '0');
    yyyy = dateToFormat.getFullYear();

    return `${yyyy}-${MM}-${dd}`;
}

function buildStringFromDate(rawDate) {
    const mm = rawDate.getMonth() + 1;
    const dd = rawDate.getDate();
    return [rawDate.getFullYear(), (mm > 9 ? '' : '0') + mm, (dd > 9 ? '' : '0') + dd].join('-');
}

function makeIdMix(length) {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}

function makeIdNumber(length) {
    let result = '';
    const characters = '0123456789';
    const charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}

module.exports = {
    addDays,
    format,
    buildStringFromDate,
    makeIdMix,
    makeIdNumber,
    formatWithValidYear
}
