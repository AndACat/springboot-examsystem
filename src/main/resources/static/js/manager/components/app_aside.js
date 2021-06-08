;(function () {
    const template = '<aside class="main-sidebar sidebar-dark-primary elevation-4">' +
        '    <!-- Brand Logo -->' +
        '    <a href="/home" class="brand-link">' +
        '      <img src="../../dist/img/AdminLTELogo.png"' +
        '           alt="ExamSystem Logo"' +
        '           class="brand-image img-circle elevation-3"' +
        '           style="opacity: .8">' +
        '      <span class="brand-text font-weight-light">在线考试系统</span>' +
        '    </a>' +
        '    <!-- Sidebar -->' +
        '    <div class="sidebar">' +
        '      <!-- Sidebar Menu -->' +
        '      <nav class="mt-2">' +
        '        <ul class="nav nav-pills nav-sidebar flex-column nav-child-indent" data-widget="treeview" role="menu" data-accordion="false">' +
        '          <!-- Add icons to the links using the .nav-icon class' +
        '               with font-awesome or any other icon font library -->' +
        '          <li class="nav-item has-treeview">' +
        '            <a href="/manager/student.html" class="nav-link">' +
        '              <i class="nav-icon fa fa-users"></i>' +
        '              <p>学生管理</p>' +
        '            </a>' +
        '          </li>' +
        '          <li class="nav-item has-treeview">' +
        '            <a href="/manager/cla.html" class="nav-link">' +
        '              <i class="nav-icon fa fa-users"></i>' +
        '              <p>班级管理</p>' +
        '            </a>' +
        '          </li>' +
        '        </ul>' +
        '      </nav>' +
        '      <!-- /.sidebar-menu -->' +
        '    </div>' +
        '    <!-- /.sidebar -->' +
        '  </aside>';
    window.app_aside={
        template
    }
})()