using Microsoft.EntityFrameworkCore.Migrations;

namespace Core.Db.Migrations
{
    public partial class Third : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "Email",
                schema: "citylogia",
                table: "Users",
                type: "text",
                nullable: true);

            migrationBuilder.AddColumn<string>(
                name: "Password",
                schema: "citylogia",
                table: "Users",
                type: "text",
                nullable: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Email",
                schema: "citylogia",
                table: "Users");

            migrationBuilder.DropColumn(
                name: "Password",
                schema: "citylogia",
                table: "Users");
        }
    }
}
